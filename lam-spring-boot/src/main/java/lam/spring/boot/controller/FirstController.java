package lam.spring.boot.controller;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lam.spring.boot.model.MyThing;
import lam.spring.boot.model.MyThing1;
import lam.spring.boot.service.IFooService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月16日
* @versio 1.0
*/
@RestController
public class FirstController {
	
	@Resource
	private IFooService fooService;
	
	@RequestMapping("/")
	public String index() {
		boolean b = fooService.dodo(new Random().nextInt(2));
		System.out.println(b);
		return "welcome!";
	}
	
	@RequestMapping("/home")
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		return mav;
	}
	
	@RequestMapping("/thing")
	public MyThing thing() {
		return new MyThing().setName("gogogo");
	}
	
	@RequestMapping("/thing1")
	public MyThing1 thing1() {
		return new MyThing1().setId(1).setName("lam");
	}

}