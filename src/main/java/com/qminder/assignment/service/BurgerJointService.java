package com.qminder.assignment.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qminder.assignment.dto.BurgerJointDTO;
import com.qminder.assignment.dto.FoursquareExplore;
import com.qminder.assignment.dto.ImageRecognitionRequest;
import com.qminder.assignment.dto.ImageRecognitionResponse;
import com.qminder.assignment.model.BurgerJoint;
import com.qminder.assignment.model.BurgerPicture;

@Service
public class BurgerJointService {
	
	
	private RestTemplate template = new RestTemplate();
	
	private List<BurgerJoint> burgerJoints = null;
	
	@Value("${exploreURL}")
	private String url;
	
	@Value("${venueURL}")
	private String venueUrl;
	
	@Value("${imageRecognitionURL}")
	private String imageRecognitionUrl;

	public List<BurgerJointDTO> getBurgerJoints() {
		FoursquareExplore result = template.getForObject(url, FoursquareExplore.class);
		List<BurgerJointDTO> joints = result.getResponse().
											getGroups().get(0).
											getItems().stream().map(v -> v.getVenue()).collect(Collectors.toList());
		return joints;
	}
	
	public String getLatestBurgerPictureOf(String venueId) {
		String photosUrl = venueUrl+venueId+"/photos";
		HttpHeaders header = new HttpHeaders();
		header.add("Cookie", "bbhive=5UCQOXQ3QO0PVCXZNH51RJR53SV30W::1663777443; _px2=eyJ1IjoiZGQ2YTRlZjAtZmMyNi0xMWVhLWFkNGQtNjc3ZDRjMWQwNzQxIiwidiI6ImRjY2FlMjk0LWZjMjYtMTFlYS04Y2ZlLTAyNDJhYzEyMDAwYSIsInQiOjE2MDA3MDU3NDM2OTQsImgiOiJlMTIyMWYwZjNkZjA0YWMzOWU1NWE3YzI4MjgyODEzNjE5YTJlNDZiMDEzMDdjMjU3YmY3YmIxMjFmMjkzM2RkIn0=; _pxff_cc=U2FtZVNpdGU9TGF4Ow==; _pxvid=dccae294-fc26-11ea-8cfe-0242ac12000a; XSESSIONID=okbk1778080~8si418oj0y3glje5mwrx0jvr; lc=%7B%22lat%22%3A58.36011831704449%2C%22lng%22%3A26.676002299157688%2C%22loc%22%3A%22Tartu%22%2C%22cc%22%3A%22EE%22%2C%22longGeoId%22%3A%22-543225100%22%7D");
		ResponseEntity<String> re = template.exchange(photosUrl,HttpMethod.GET,new HttpEntity<String>(header),String.class);
		String response = re.getBody();
		Pattern p = Pattern.compile("<div class=\"date.*?\">(.*?)</div></div></div><img (.*?)></span>");
		Matcher m = p.matcher(response);
		List<BurgerPicture> list = new ArrayList<BurgerPicture>();
		
		while(m.find()) {
			String posted = m.group(1);
			String link = m.group(2);
			list.add(from(posted,link));
		}
		List<String> urls = list.stream().sorted().map(bp -> bp.getLink()).collect(Collectors.toList());
		ImageRecognitionRequest request = new ImageRecognitionRequest();
		request.setUrls(urls);
		return getUrlWithBurger(request);
	}
	
	public String getUrlWithBurger(ImageRecognitionRequest request) {
		try {
			ImageRecognitionResponse response = template.postForObject(imageRecognitionUrl, request, ImageRecognitionResponse.class);
			return response.getUrlWithBurger();
		}catch(Exception e) {
				return "";
		}
	}
	
	public List<BurgerJoint> getBurgerJointsForDisplay() {
		if(this.burgerJoints == null || this.burgerJoints.isEmpty()) setBurgerJointsForDisplay();
		return this.burgerJoints;
	}
	
	@Scheduled(fixedRate = 1000*60*60)
	public void setBurgerJointsForDisplay() {
		List<BurgerJointDTO> joints = getBurgerJoints();
		this.burgerJoints = joints.stream().map(bj -> new BurgerJoint(bj.getName(),bj.getLocation().getAddress(),getLatestBurgerPictureOf(bj.getId())))
				.sorted().collect(Collectors.toList());
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
