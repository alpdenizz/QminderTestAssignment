package com.qminder.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qminder.assignment.dto.ImageRecognitionRequest;
import com.qminder.assignment.model.BurgerPicture;
import com.qminder.assignment.service.BurgerJointService;

@SpringBootTest
class BurgerJointsInTartuApplicationTests {
	
	@Autowired
	private BurgerJointService service;
	
	/*@Test
	void jointsRetrieved() {
		List<BurgerJointDTO> list = service.getBurgerJoints();
		assertEquals(true, list.size() > 0);
		list.forEach(b -> System.out.println(b.toString()));
	}*/
	
	/*@Test
	void getAllPictures() throws Exception {
		String test = "563c879fcd102b8e3528f039";
		String first = service.getLatestBurgerPictureOf(test);
		assertNotNull(first);
		assertFalse(first.isEmpty());
		//Files.write(Paths.get("./response.html"), first.getBytes());
		Pattern p = Pattern.compile("<div class=\"date.*?\">(.*?)</div></div></div><img (.*?)></span>");
		
		Matcher m = p.matcher(first);
		assertEquals(true, m.find());
		
		List<BurgerPicture> list = new ArrayList<BurgerPicture>();
		String posted = m.group(1);
		String link = m.group(2);
		
		list.add(from(posted,link));
		
		while(m.find()) {
			posted = m.group(1);
			link = m.group(2);
			list.add(from(posted,link));
		}
		
		List<String> urls = list.stream().sorted().map(bp -> bp.getLink()).collect(Collectors.toList());
		ImageRecognitionRequest request = new ImageRecognitionRequest();
		request.setUrls(urls);
		String urlWithBurger = service.getUrlWithBurger(request);
		assertNotNull(urlWithBurger);
		assertFalse(urlWithBurger.isEmpty());
		System.out.println(urlWithBurger);
	}*/
	
	public static BurgerPicture from(String posted, String link) {
		BurgerPicture pic = new BurgerPicture();
		if(posted.indexOf(',') == -1) {
			String[] arr = posted.split(" ");
			Month month = Month.valueOf(arr[0].toUpperCase());
			int day = Integer.valueOf(arr[1]);
			int year = LocalDate.now().getYear();
			pic.setPosted(LocalDate.of(year, month, day));
		}
		else {
			String[] arr = posted.split(" ");
			Month month = Month.valueOf(arr[0].toUpperCase());
			int day = Integer.valueOf(arr[1].substring(0, arr[1].length()-1));
			int year = Integer.valueOf(arr[2]);
			pic.setPosted(LocalDate.of(year, month, day));
		}
		int fq = link.indexOf('"');
		int sq = link.indexOf('"', fq+1);
		pic.setLink(link.substring(fq+1, sq));
		return pic;
	}

}
