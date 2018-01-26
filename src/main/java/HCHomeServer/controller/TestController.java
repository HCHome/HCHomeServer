package HCHomeServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import HCHomeServer.model.result.ResultData;

@Controller
public class TestController {

	@RequestMapping(value="/test")
	@ResponseBody
	public ResultData test() {
		return ResultData.build_success_result(null);
	}
}
