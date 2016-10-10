package lam.netty.http.file.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

/**
* <p>
* http file server handler
* </p>
* @author linanmiao
* @date 2016年10月7日
* @versio 1.0
*/
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

	private static Logger logger = LoggerFactory.getLogger(HttpFileServerHandler.class);
	
	private final String url;
	
	public HttpFileServerHandler(String url) {
		this.url = url;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if(!request.decoderResult().isSuccess()){
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return ;
		}
		if(!request.method().equals(HttpMethod.GET)){
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return ;
		}
		final String uri = request.uri();
		final String path = sanitizeUri(uri);
		if(path == null){
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return ;
		}
		File file = new File(path);
		if(file.isHidden() || !file.exists()){
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return ;
		}
		if(file.isDirectory()){
			if(uri.endsWith("/")){
				sendListing(ctx, file);
			}else{
				sendRedirect(ctx, uri + '/');
			}
			return ;
		}
		if(!file.isFile()){
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return ;
		}
		RandomAccessFile randomAccessFile = null;
		try{
			randomAccessFile = new RandomAccessFile(file, "r");
		}catch(FileNotFoundException e){
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return ;
		}
		long fileLength = randomAccessFile.length();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength);
		setContentTypeHeader(response, file);
		//response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		ctx.write(response);
		
		ChannelFuture f = ctx.write(
				new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
	
		f.addListener(new ChannelProgressiveFutureListener(){
			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
					throws Exception {
				if(total < 0){
					logger.info("Transfer progress:" + progress);
				}else{
					logger.info("Transfer progress:{}/{}", progress, total);
				}
			}
			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				logger.info("Transfer complete.");
			}});
		
		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		lastContentFuture.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught error==>>", cause);
		if(ctx.channel().isActive()){
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private void setContentTypeHeader(HttpResponse response, File file){
		MimetypesFileTypeMap mime = new MimetypesFileTypeMap();
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mime.getContentType(file.getPath()));
	}
	
	private void sendRedirect(ChannelHandlerContext ctx, String uri){
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaderNames.LOCATION, uri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	private void sendListing(ChannelHandlerContext ctx, File file) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		String path = file.getPath();
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\r\n")
		.append("<html><head><title>")
		.append(path)
		.append(" directory:")
		.append("</title></head><body>\r\n")
		.append("<h3>")
		.append(path)
		.append(" directory:")
		.append("</h3>\r\n")
		.append("<ul>")
		.append("<li>link:<a href=\"../\">..</a></li>\r\n");
		for(File f : file.listFiles()){
			if(f.isHidden() || !f.canRead()){
				continue;
			}
			String name = f.getName();
			if(!ALLOWED_FILE_NAME.matcher(name).matches()){
				continue;
			}
			sb.append("<li>link:<a href=\"")
			.append(name)
			.append("\">")
			.append(name)
			.append("</a></li>\r\n");
		}
		sb.append("</ul></body></html>\r\n");
		ByteBuf byteBuf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
		response.content().writeBytes(byteBuf);
		byteBuf.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	private String sanitizeUri(String uri){
		try {
			uri = URLDecoder.decode(uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("sanitizaUri error==>>", e);
			try {
				uri = URLDecoder.decode(uri, "iso-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error("do not support iso-8859-1");
			}
		}
		if(!uri.startsWith(url)){
			return null;
		}
		if(!uri.startsWith("/")){
			return null;
		}
		uri = uri.replace('/', File.separatorChar);
		if(uri.contains(File.separator + '.')
		   || uri.contains('.' + File.separator) || uri.startsWith(".")
		   || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()){
			return null;
		}
		
		return System.getProperty("user.dir") + File.separator + uri;
	}
	
	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	
	private static final Pattern ALLOWED_FILE_NAME =
			Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
	
	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
