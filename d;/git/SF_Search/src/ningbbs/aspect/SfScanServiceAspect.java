package ningbbs.aspect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ningbbs.gui.MainGui;
import ningbbs.kd.SfSearch;
import ningbbs.kd.SfSearchUtil;
import ningbbs.service.SfScanService;
import ningbbs.util.Constants;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SfScanServiceAspect {
	@Pointcut("execution(* ningbbs.service.SfScanService.scan(..))")
	private void scan() {}
	@Pointcut("execution(* ningbbs.service.SfScanService.execute(..))")
	private void execute() {}
	@SuppressWarnings("unchecked")
	
	@Around("scan()")
	public List<SfSearch> scan(ProceedingJoinPoint pjp){
		List<SfSearch> list=new ArrayList<SfSearch>();
		try {
			SfScanService service= (SfScanService) pjp.getArgs()[0];
			list= (List<SfSearch>) pjp.proceed();
			Iterator<SfSearch> iter = list.iterator();  
			while(iter.hasNext()){  
				SfSearch sf = iter.next();  
				if(sf.getRoutes()==null || sf.getRoutes().size()<=0){
					iter.remove();
					continue;
				}
				if(handleSfSearch(sf,service)){
					String msg="★加入数据库:进行发送处理["+sf.getId()+"]★";
					Constants.sendMsg(msg, false, false);
					if(MainGui.user!=null){
						MainGui.user.addPool(sf);
					}
				}else{
					System.out.println("不符合要求 :"+sf.getId()+" "+SfScanService.getScanTime(sf)+" 当前未处理:");
					iter.remove();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}
	//分析是否合格
	public boolean handleSfSearch(SfSearch sf,SfScanService service){
		try {
			boolean flag=false;
			//判断类型是否符合
			String limitType=sf.getLimitTypeCode();
			if(limitType==null || limitType.equals("")){
				limitType="null";
				sf.setLimitTypeCode(limitType);
			}
			System.out.println(Constants.TypeCodeArr);
			for(int i=0;i<Constants.TypeCodeArr.length;i++){
				String type=Constants.TypeCodeArr[i];
				if(limitType.startsWith(type)){
					flag=true;
					break;
				}
			}
			
			if(flag){
				if(SfScanService.getScanTime(sf)==null){
					System.out.println("无扫描时间");
					return false;
				}
				return  SfSearchUtil.testSearch(sf,service);
			}else{
				System.out.print("类型不符合["+limitType+"]");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
