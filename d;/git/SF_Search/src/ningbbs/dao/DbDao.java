package ningbbs.dao;

import java.util.List;

import ningbbs.kd.SfSearch;

public interface DbDao {
	/**
	 * ����һ����
	 * @param kd
	 */
	public void addOlder(SfSearch kd);
	/**
	 * ���ݸ����Ķ� ���� ��
	 * @param id
	 * @return
	 */
	public List<SfSearch> getOlder(String id);
	/**
	 * ����������Ϣ
	 * @param sf
	 */
	public void addWdInfo(SfSearch sf);
	/**
	 * ������
	 * @param info
	 * @param shi
	 */
	public void addShi(String info, String shi);
	/**
	 * ������
	 * @param info
	 * @param qu
	 */
	public void addQu(String info, String qu);
	/**
	 * ����ʡ
	 * @param info
	 * @param sheng
	 */
	public void addSheng(String info, String sheng);
	/**
	 * ����IDS��Ϣ
	 * @param info
	 * @param ids
	 */
	public void updataIds(String info,String ids);
	/**
	 * ȡ��Ӧinfo��ids��Ϣ
	 * @param info
	 * @return
	 */
	public String getWdIds(String info);
	/**
	 * ȡsf_wd��info�б�
	 * @return
	 */
	public List<String> getSf_Wd();
	/**
	 * ȡkdnum��Ŀ�ݵ����б�
	 * @return
	 */
	public List<String> getKdnum(String date);
}
