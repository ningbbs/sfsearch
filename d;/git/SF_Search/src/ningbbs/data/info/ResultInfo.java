package ningbbs.data.info;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * ������Ϣ��
 * 
 * @author shining
 *
 */
public class ResultInfo {
	/**
	 * ���ؽ��(����ֵ)
	 */
	private int status;
	/**
	 * ������Ϣ
	 */
	private String info;
	/**
	 * �����쳣(����쳣����addException�������)
	 */
	private List<Exception> exceptionList;
	/**
	 * ���صĸ�������
	 */
	private Object extData;
	/**
	 * ��չ��������
	 */
	private Type ext_Type;

	/**
	 * ����һ���쳣��Ϣ
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
	 * ȡ״̬��Ϣ
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * ����״̬��Ϣ
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * ȡ��ʾ��Ϣ
	 * 
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * ������ʾ��Ϣ
	 * 
	 * @param info
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * ȡ�쳣�б�
	 * 
	 * @return
	 */
	public List<Exception> getExceptionList() {
		return exceptionList;
	}

	/**
	 * �����쳣�б�
	 * 
	 * @param exceptionList
	 */
	public void setExceptionList(List<Exception> exceptionList) {
		this.exceptionList = exceptionList;
	}

	/**
	 * ȡ��չ����
	 * 
	 * @return
	 */
	public Object getExtData() {
		return extData;
	}

	/**
	 * ������չ����
	 * 
	 * @param extData
	 */
	public void setExtData(Object extData, Type ext_Type) {
		this.extData = extData;
		this.ext_Type = ext_Type;
	}

	/**
	 * ȡ��չ����
	 * 
	 * @return
	 */
	public Type getExt_Type() {
		return ext_Type;
	}

	/**
	 * ������չ����
	 * 
	 * @param ext_Type
	 */
	public void setExt_Type(Type ext_Type) {
		this.ext_Type = ext_Type;
	}
}
