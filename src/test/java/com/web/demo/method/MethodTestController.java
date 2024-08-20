package com.web.demo.method;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// FIXME #REFACT: Get, Post, Put, Delete 테스트, URL 데이터 전송 방식 테스트
@Controller
public class MethodTestController {
	/**
	 * get방식 처리하는 매핑 메소드 구현
	 * @GetMapping
	 * http://localhost:8080/news?mediaId=1234&serviceType=SPORTS
	 * get방식으로 전달하는 파라미터는? mediaId, serviceType 2개
	 * 전달된 데이터가 메소드 내부로 전달되는 방식? 파라미터를 통해서 전달 => 입력
	 * @RequestParam(value="키") String 값을대변한변수명
	 */
	@GetMapping("/news")
	@ResponseBody
	public String news(@RequestParam(value="mediaId") String id, 
			           @RequestParam(value="serviceType") String st) {
		// 차후 뉴스 목록을 디비에서 조회하여 html에 세팅하여 동적 구성후 응답
		// 전달된 데이터를 가지고 특정 뉴스를 조회한다 <= 비즈니스 로직
		// ex) select * from news where mediaId='1234' and serviceType='SPORTS'; 
		return "뉴스 " + id + " " + st;
	}
	
	/**
	 * url에 데이터를 전달
	 * 	- 매핑값추가 : /news2/{id}
	 *  - 인자값추가 : @PathVariable("id") Integer authId
	 * @param id 
	 * @param st 
	 * @return 
	 */
	@GetMapping("/news2/{id}")
	@ResponseBody
	public String news2(@PathVariable("id") Integer authId,
			            @RequestParam(value="mediaId") String id, 
			            @RequestParam(value="serviceType") String st) {
		// 차후 뉴스 목록을 디비에서 조회하여 html에 세팅하여 동적 구성후 응답
		// 전달된 데이터를 가지고 특정 뉴스를 조회한다 <= 비즈니스 로직
		// ex) select * from news where mediaId='1234' and serviceType='SPORTS'; 
		return authId + "뉴스2 " + id + " " + st;
	}
	
	/**
	 *  
	 *  - URL : /join
  		- method : post
  		- 메소드명 : join
  		- 데이터 전송은 form 전송
    	- uid, upw 값 전달
    	- vscode > thunder client 활용
    	- 응답은 알아서 구성
  		- 실습 3분 (메소드만 구현)
	 */
	@PostMapping("/join")
	@ResponseBody
	public String join( @RequestParam(value="uid") String uid, 
			            @RequestParam(value="upw") String upw) {
		return "join " + uid + " " + upw;
	}
	
	/**
	 * URL path를 통한 데이터 전달 n개 가능
	 * URL path 형식에 맞지 않고 요청하면 -> 404
	 * 	=> http://localhost:8080/join2/1    <= 404
	 * 	=> http://localhost:8080/join2/1/2  <= OK 
	 * @param id
	 * @param pid
	 * @param uid
	 * @param upw
	 * @return
	 */
	@PostMapping("/join2/{id}/{pid}")
	@ResponseBody
	public String join2( @PathVariable("id") Integer id,
						 @PathVariable("pid") Integer pid,
						 @RequestParam(value="uid") String uid, 
			             @RequestParam(value="upw") String upw) {
		System.out.print(id + " " + pid);
		return "join " + uid + " " + upw;
	}
}
