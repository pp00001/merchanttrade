package ins.platform.aggpay.trade.vo;

import java.util.Map;

import com.mybank.bkmerchant.base.AbstractReq;


public class PublicCall extends AbstractReq{
 
	public final static String FUNCTION_MERCHANTQUERY = "ant.mybank.merchantprod.merchant.query";
	public final static String FUNCTION_FREEZE = "ant.mybank.merchantprod.merchant.freeze";
	public final static String FUNCTION_UNFREEZE = "ant.mybank.merchantprod.merchant.unfreeze";
//	public final static String FUNCTION_FREEZE = "ant.mybank.merchantprod.merchant.freeze";

	
	private Map<String, String> body;
	
	public PublicCall(String function) {
		super(function);
		
	}

	@Override
	public Map<String, String> getBody() {
		return this.body;
	}

	public void setBody(Map<String, String> body){
		this.body = body;
	}
}
