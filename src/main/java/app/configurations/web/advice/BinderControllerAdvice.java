package app.configurations.web.advice;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @desc Spring4Shell 취약점 조치
 */
@ControllerAdvice
@Order(10000)
public class BinderControllerAdvice {

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		String[] denylist = new String[] { "class.", "Class.", ".class.", ".Class." };
		dataBinder.setDisallowedFields(denylist);
	}// end of setAllowedFields
	
}
