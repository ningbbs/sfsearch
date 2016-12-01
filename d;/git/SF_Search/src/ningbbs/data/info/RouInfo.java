package ningbbs.data.info;

//路由信息
public class RouInfo {
	String name;//路由名称 **营业点
	int index; //过些,在路线中是第几个
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
