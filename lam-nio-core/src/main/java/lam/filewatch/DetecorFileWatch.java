package lam.filewatch;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
* <p>
* watch file modify. jdk1.7 above.
* </p>
* @author linanmiao
* @date 2017年11月6日
* @version 1.0
*/
public class DetecorFileWatch {
	
	public static void main(String[] args){
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Paths.get("D:\\sky").register(watcher,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			while (true) {
				WatchKey watchKey = watcher.take();
				List<WatchEvent<?>> events = watchKey.pollEvents();
				for (WatchEvent<?> event : events) {
					System.out.println(event.context() + " come to " + event.kind());
				}
				
				boolean valid = watchKey.reset();
				if (!valid) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
