package ningbbs.data.info;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回信息类
 * 
 * @author shining
 *
 */
public class ResultInfo {
	/**
	 * 返回结果(整数值)
	 */
	private int status;
	/**
	 * 返回信息
	 */
	private String info;
	/**
	 * 返回异常(添加异常请在addException方法添加)
	 */
	private List<Exception> exceptionList;
	/**
	 * 返回的附加数据
	 */
	private Object extData;
	/**
	 * 扩展数据类型
	 */
	private Type ext_Type;

	/**
	 * 增加一个异常信息
	 * 
	 * @param e
	 */
	public void addException(Exception e) {
		if (exceptionList == null) {
			exceptionList = new ArrayList<>();
		}
		exceptionList.add(e);
	}

	/**
	 * 取状态信息
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态信息
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 取提示消息
	 * 
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * 设置提示消息
	 * 
	 * @param info
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * 取异常列表
	 * 
	 * @return
	 */
	public List<Exception> getExceptionList() {
		return exceptionList;
	}

	/**
	 * 设置异常列表
	 * 
	 * @param exceptionList
	 */
	public void setExceptionList(List<Exception> exceptionList) {
		this.exceptionList = exceptionList;
	}

	/**
	 * 取扩展数据
	 * 
	 * @return
	 */
	public Object getExtData() {
		return extData;
	}

	/**
	 * 设置扩展数据
	 * 
	 * @param extData
	 */
	public void setExtData(Object extData, Type ext_Type) {
		this.extData = extData;
		this.ext_Type = ext_Type;
	}

	/**
	 * 取扩展类型
	 * 
	 * @return
	 */
	public Type getExt_Type() {
		return ext_Type;
	}

	/**
	 * 设置扩展类型
	 * 
	 * @param ext_Type
	 */
	public void setExt_Type(Type ext_Type) {
		this.ext_Type = ext_Type;
	}
}
