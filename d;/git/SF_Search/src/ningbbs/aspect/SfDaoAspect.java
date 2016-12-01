package ningbbs.aspect;


import java.util.regex.Pattern;

import ningbbs.kd.SfSearch;
import ningbbs.service.SfScanService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SfDaoAspect {
	//public static Pattern p=Pattern.compile("【.*?】.*?已装车，准备发往 【.*?】");
	public static Pattern p=Pattern.compile("【.*?】</font></a>已装车，准备发往");
	@Pointcut("execution(* ningbbs.dao.SfDaoImpl.addOlder(..))")
	 private void actionMethod() {}
	
	@Around("actionMethod()")
	public void add(ProceedingJoinPoint pjp){
		Object[] params=pjp.getArgs();
		SfSearch sf=(SfSearch) params[0];
		log(sf);
		try {
			pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	//打印或日志
	public void log(SfSearch sf){
		try {
			String message=sf.getId()+" "+SfScanService.getScanTime(sf)+" ["+ sf.getProductName()+"] [" +sf.getLimitTypeName()+":"+sf.getLimitTypeCode()+"] ["+ sf.getOrigin()+"] 至 ["+sf.getDestination()+"]";
			System.out.println(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
