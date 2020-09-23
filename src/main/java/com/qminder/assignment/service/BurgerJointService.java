package com.qminder.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qminder.assignment.dto.BurgerJointDTO;
import com.qminder.assignment.dto.FoursquareExplore;
import com.qminder.assignment.model.BurgerJoint;

/**
 * Main service for this application. It communicates with async service for heavy operations.
 *
 */
@Service
public class BurgerJointService {
	
	private RestTemplate template = new RestTemplate();
	
	private static final Logger logger = LoggerFactory.getLogger(BurgerJointService.class);
	
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
	
	//Get Burger Joints from Foursquare Explore API
	public List<BurgerJointDTO> getBurgerJoints() {
		FoursquareExplore result = template.getForObject(url, FoursquareExplore.class);
		List<BurgerJointDTO> joints = result.getResponse().
											getGroups().get(0).
											getItems().stream().map(v -> v.getVenue()).collect(Collectors.toList());
		return joints;
	}
	
	public List<BurgerJoint> getBurgerJointsForDisplay() throws Exception {
		if(this.burgerJoints == null || this.burgerJoints.isEmpty()) setBurgerJointsForDisplay();
		return this.burgerJoints;
	}
	
	/**
	 * After 1 hour the application started, this retries burger joints with pictures to capture the latest ones.
	 * Then retries this procedures with 1 hour interval.
	 */
	@Scheduled(initialDelay=3600000, fixedRate=3600000)
	public void setBurgerJointsForDisplay() throws Exception {
		logger.info("[+] Setting burger joints for display...");
		
		List<BurgerJointDTO> joints = getBurgerJoints();
		List<CompletableFuture<BurgerJoint>> completableList = new ArrayList<CompletableFuture<BurgerJoint>>();
		
		for(BurgerJointDTO bj : joints) {
			completableList.add(asyncService.getBurgerJoint(bj.getId(),bj.getName(),bj.getLocation().getAddress()));
		}
		
		//Wait until all burger joints are ready with their picture
		CompletableFuture.allOf((CompletableFuture[]) completableList.toArray(new CompletableFuture[completableList.size()])).join();
		
		List<BurgerJoint> list = new ArrayList<BurgerJoint>();
		for(CompletableFuture<BurgerJoint> c : completableList) {
			list.add(c.get());
		}
		
		list.sort(null);
		this.burgerJoints = list;
	}
	
}
