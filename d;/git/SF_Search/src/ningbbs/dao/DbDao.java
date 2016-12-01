package ningbbs.dao;

import java.util.List;

import ningbbs.kd.SfSearch;

public interface DbDao {
	/**
	 * 增加一个单
	 * @param kd
	 */
	public void addOlder(SfSearch kd);
	/**
	 * 根据给定的段 搜索 单
	 * @param id
	 * @return
	 */
	public List<SfSearch> getOlder(String id);
	/**
	 * 加入网点信息
	 * @param sf
	 */
	public void addWdInfo(SfSearch sf);
	/**
	 * 增加市
	 * @param info
	 * @param shi
	 */
	public void addShi(String info, String shi);
	/**
	 * 增加区
	 * @param info
	 * @param qu
	 */
	public void addQu(String info, String qu);
	/**
	 * 增加省
	 * @param info
	 * @param sheng
	 */
	public void addSheng(String info, String sheng);
	/**
	 * 更新IDS信息
	 * @param info
	 * @param ids
	 */
	public void updataIds(String info,String ids);
	/**
	 * 取对应info的ids信息
	 * @param info
	 * @return
	 */
	public String getWdIds(String info);
	/**
	 * 取sf_wd的info列表
	 * @return
	 */
	public List<String> getSf_Wd();
	/**
	 * 取kdnum表的快递单号列表
	 * @return
	 */
	public List<String> getKdnum(String date);
}
