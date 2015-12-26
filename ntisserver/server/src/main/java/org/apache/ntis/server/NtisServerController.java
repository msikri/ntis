package org.apache.ntis.server;

import org.apache.ntis.log.time.BasicTimedInterceptor;
import org.apache.ntis.server.datamodel.GetProximityList;
import org.apache.ntis.server.datamodel.GetProximityListResponse;
import org.eclipse.ntis.server.NTISAlgo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class NtisServerController {

	public NtisServerController() {
		System.out.println("Starting NTIS Server Controller.....");
	}

	final String NAMESPACE_URI = "http://www.ntis.org/schema";

	@Autowired
	private BasicTimedInterceptor timedInterceptor;

	@RequestMapping(value = "ntis", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	GetProximityListResponse call(@RequestBody GetProximityList list) {
		timedInterceptor.startMethod();
		try {
			NTISAlgo algo = new NTISAlgo();
			GetProximityListResponse responseob = algo.processMessage(list);
			return responseob;
		} finally {
			timedInterceptor.endMethod("NtisServer");
		}
	}

}