package ningbbs.data.info;

//·����Ϣ
public class RouInfo {
	String name;//·������ **Ӫҵ��
	int index; //��Щ,��·�����ǵڼ���
	public RouInfo(String name,int index){
		this.name=name;
		this.index=index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
