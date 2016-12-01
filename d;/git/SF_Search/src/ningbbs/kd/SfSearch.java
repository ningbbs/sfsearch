package ningbbs.kd;

import java.util.Date;
import java.util.List;

/**
 * Ë³·á¹ÙÍø²éÑ¯
 * 
 * @author Administrator
 *
 */
public class SfSearch implements KD{
	String id;
	String origin;
	String originIds;
	String originCode;
	String destination;
	String destinationIds;
	String receiveBillFlg;
	String delivered;
	String expectedDeliveryTime;
	String refundable;
	String limitTypeCode;
	String limitTypeName;
	String mainlandToMainland;
	List<Routes> routes;
	String prioritized;
	String warehouse;
	String signed;
	String expressState;
	String lstElementHtml;
	List<ChildSet> childSet;
	String showThermometer;
	String productCode;
	String productName;
	String billFlag;
	String recipientTime;
	Date addtime;

	public class Routes {
		String scanDateTime;
		String remark;
		String stayWhyCode;
		@Override
		public String toString() {
			return scanDateTime+":"+remark+":"+stayWhyCode;
		}
		public String getScanDateTime() {
			return scanDateTime;
		}
		
	}

	class ChildSet {

	}

	@Override
	public String toString() {
		return "SfSearch [id=" + id + ", origin=" + origin + ", originIds="
				+ originIds + ", originCode=" + originCode + ", destination="
				+ destination + ", destinationIds=" + destinationIds
				+ ", receiveBillFlg=" + receiveBillFlg + ", delivered="
				+ delivered + ", expectedDeliveryTime=" + expectedDeliveryTime
				+ ", refundable=" + refundable + ", limitTypeCode="
				+ limitTypeCode + ", limitTypeName=" + limitTypeName
				+ ", mainlandToMainland=" + mainlandToMainland + ", routes="
				+ routes + ", prioritized=" + prioritized + ", warehouse="
				+ warehouse + ", signed=" + signed + ", expressState="
				+ expressState + ", lstElementHtml=" + lstElementHtml
				+ ", childSet=" + childSet + ", showThermometer="
				+ showThermometer + ", productCode=" + productCode
				+ ", productName=" + productName + ", billFlag=" + billFlag
				+ ", recipientTime=" + recipientTime + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOriginCode() {
		return originCode;
	}

	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getReceiveBillFlg() {
		return receiveBillFlg;
	}

	public void setReceiveBillFlg(String receiveBillFlg) {
		this.receiveBillFlg = receiveBillFlg;
	}

	public String getDelivered() {
		return delivered;
	}

	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	public String getExpectedDeliveryTime() {
		return expectedDeliveryTime;
	}

	public void setExpectedDeliveryTime(String expectedDeliveryTime) {
		this.expectedDeliveryTime = expectedDeliveryTime;
	}

	public String getRefundable() {
		return refundable;
	}

	public void setRefundable(String refundable) {
		this.refundable = refundable;
	}

	public String getLimitTypeCode() {
		return limitTypeCode;
	}

	public void setLimitTypeCode(String limitTypeCode) {
		this.limitTypeCode = limitTypeCode;
	}

	public String getLimitTypeName() {
		return limitTypeName;
	}

	public void setLimitTypeName(String limitTypeName) {
		this.limitTypeName = limitTypeName;
	}

	public String getMainlandToMainland() {
		return mainlandToMainland;
	}

	public void setMainlandToMainland(String mainlandToMainland) {
		this.mainlandToMainland = mainlandToMainland;
	}

	public List<Routes> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Routes> routes) {
		this.routes = routes;
	}

	public String getPrioritized() {
		return prioritized;
	}

	public void setPrioritized(String prioritized) {
		this.prioritized = prioritized;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

	public String getExpressState() {
		return expressState;
	}

	public void setExpressState(String expressState) {
		this.expressState = expressState;
	}

	public String getLstElementHtml() {
		return lstElementHtml;
	}

	public void setLstElementHtml(String lstElementHtml) {
		this.lstElementHtml = lstElementHtml;
	}

	public List<ChildSet> getChildSet() {
		return childSet;
	}

	public void setChildSet(List<ChildSet> childSet) {
		this.childSet = childSet;
	}

	public String getShowThermometer() {
		return showThermometer;
	}

	public void setShowThermometer(String showThermometer) {
		this.showThermometer = showThermometer;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}

	public String getOriginIds() {
		return originIds;
	}

	public void setOriginIds(String originIds) {
		this.originIds = originIds;
	}

	public String getDestinationIds() {
		return destinationIds;
	}

	public void setDestinationIds(String destinationIds) {
		this.destinationIds = destinationIds;
	}

	public String getRecipientTime() {
		return recipientTime;
	}

	public void setRecipientTime(String recipientTime) {
		this.recipientTime = recipientTime;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

}
