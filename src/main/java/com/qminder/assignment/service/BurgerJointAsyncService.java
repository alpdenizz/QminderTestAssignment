package com.qminder.assignment.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qminder.assignment.dto.ImageRecognitionRequest;
import com.qminder.assignment.dto.ImageRecognitionResponse;
import com.qminder.assignment.model.BurgerJoint;
import com.qminder.assignment.model.BurgerPicture;

@Service
public class BurgerJointAsyncService {
	
	private static final Logger logger = LoggerFactory.getLogger(BurgerJointAsyncService.class);
	
	private RestTemplate template = new RestTemplate();
	
	@Value("${exploreURL}")
	private String url;
	
	@Value("${venueURL}")
	private String venueUrl;
	
	@Value("${imageRecognitionURL}")
	private String imageRecognitionUrl;
	
	@Value("${px2}")
	private String px2;
	
	@Value("${pxff_cc}")
	private String pxff_cc;
	
	@Value("${pxvid}")
	private String pxvid;
	
	@Value("${bbhive}")
	private String bbhive;
	
	@Value("${lc}")
	private String lc;
	
	@Value("${xsessionid}")
	private String xsessionid;
	
	@Async
	public CompletableFuture<BurgerJoint> getBurgerJoint(String venueId, String name, String address) {
		logger.info("[+] Looking for photo...");
		String photosUrl = venueUrl+venueId+"/photos";
		HttpHeaders header = new HttpHeaders();
		String cookie = String.format("bbhive=%s; _px2=%s; _pxff_cc=%s; _pxvid=%s; XSESSIONID=%s; lc=%s;", bbhive, px2, pxff_cc, pxvid, xsessionid, lc);
		header.add("Cookie", cookie);
		header.add("Accept", "text/html");
		header.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0");
		ResponseEntity<String> re = template.exchange(photosUrl,HttpMethod.GET,new HttpEntity<String>(header),String.class);
		String response = re.getBody();
		logger.info("[+] HTML content retrieved...");
		Pattern p = Pattern.compile("<div class=\"date.*?\">(.*?)</div></div></div><img (.*?)></span>");
		Matcher m = p.matcher(response);
		List<BurgerPicture> list = new ArrayList<BurgerPicture>();
		
		while(m.find()) {
			String posted = m.group(1);
			String link = m.group(2);
			list.add(from(posted,link));
		}
		List<String> urls = list.stream().sorted().map(bp -> bp.getLink()).collect(Collectors.toList());
		logger.info("[+] Photos URL: "+photosUrl);
		ImageRecognitionRequest request = new ImageRecognitionRequest();
		request.setUrls(urls);
		
		try {
			ImageRecognitionResponse imageResponse = template.postForObject(imageRecognitionUrl, request, ImageRecognitionResponse.class);
			CompletableFuture.completedFuture(new BurgerJoint(name, address, imageResponse.getUrlWithBurger()));
		}catch(Exception e) {
			logger.info("[+] No photo for this place");
			return CompletableFuture.completedFuture(new BurgerJoint(name, address, ""));
		}
		return CompletableFuture.completedFuture(new BurgerJoint(name, address, ""));
	}
	
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
