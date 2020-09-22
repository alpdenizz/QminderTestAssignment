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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
	
	private static final Logger logger = LoggerFactory.getLogger(BurgerJointService.class);
	
	private RestTemplate template = new RestTemplate();
	
	private List<BurgerJoint> burgerJoints = null;
	
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
	
	@Autowired
	private BurgerJointAsyncService asyncService;

	public List<BurgerJointDTO> getBurgerJoints() {
		FoursquareExplore result = template.getForObject(url, FoursquareExplore.class);
		List<BurgerJointDTO> joints = result.getResponse().
											getGroups().get(0).
											getItems().stream().map(v -> v.getVenue()).collect(Collectors.toList());
		return joints;
	}
	
	@Async("asyncExec")
	public CompletableFuture<String> getLatestBurgerPictureOf(String venueId) {
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
		return CompletableFuture.completedFuture(getUrlWithBurger(request));
	}
	
	public String getUrlWithBurger(ImageRecognitionRequest request) {
		try {
			ImageRecognitionResponse response = template.postForObject(imageRecognitionUrl, request, ImageRecognitionResponse.class);
			return response.getUrlWithBurger();
		}catch(Exception e) {
			logger.info("[+] No photo for this place");
			return "";
		}
	}
	
	public List<BurgerJoint> getBurgerJointsForDisplay() throws Exception {
		if(this.burgerJoints == null || this.burgerJoints.isEmpty()) setBurgerJointsForDisplay();
		return this.burgerJoints;
	}
	
	public void setBurgerJointsForDisplay() throws Exception {
		List<BurgerJointDTO> joints = getBurgerJoints();
		List<CompletableFuture<BurgerJoint>> completableList = new ArrayList<CompletableFuture<BurgerJoint>>();
		
		for(BurgerJointDTO bj : joints) {
			completableList.add(asyncService.getBurgerJoint(bj.getId(),bj.getName(),bj.getLocation().getAddress()));
		}
		
		CompletableFuture.allOf((CompletableFuture[]) completableList.toArray(new CompletableFuture[completableList.size()])).join();
		List<BurgerJoint> list = new ArrayList<BurgerJoint>();
		for(CompletableFuture<BurgerJoint> c : completableList) {
			list.add(c.get());
		}
		list.sort(null);
		this.burgerJoints = list;
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
