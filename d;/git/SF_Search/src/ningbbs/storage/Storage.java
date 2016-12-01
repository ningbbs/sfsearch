package ningbbs.storage;

import java.util.LinkedList;  

import ningbbs.data.info.User;
import ningbbs.kd.SfSearch;
import ningbbs.service.SfScanService;

/** 
 * 仓库类Storage实现缓冲区 
 *  
 */  
public class Storage  
{  
    // 仓库最大存储量  
    private final int MAX_SIZE = 10;  
    // 仓库存储的载体  
    private LinkedList<SfSearch> list = new LinkedList<SfSearch>();  //可更改为其它类型
    // 生产num个产品  
    public void produce(SfScanService scanService) throws Exception 
    {  
        // 同步代码段  
        synchronized (list)  
        {  
            // 如果仓库剩余容量不足  
            while (list.size() > MAX_SIZE)  
            {  
               System.out.println("【库存量】:"   + list.size() + "\t暂时不能执行生产任务!");  
                try  
                {  
                    // 由于条件不满足，生产阻塞  
                    list.wait();  
                }  
                catch (InterruptedException e)  
                {  
                    e.printStackTrace();  
                }  
            }
            System.err.println("		■生产一个 【生产后仓储量为】:" + list.size());  
            list.addAll(scanService.scan(scanService)); //生产
            list.notifyAll();  
        }  
    }  
  
    // 消费num个产品  
    public SfSearch consume(User user)  throws Exception 
    {  
    	SfSearch sf=null;
        // 同步代码段  
        synchronized (list)  
        {  
            // 如果仓库存储量不足  
            while (list.size() < 1)  
            {  
                System.out.println("【库存量】:"   + list.size() + "\t暂时不能执行消费任务!");  
                try  
                {  
                    // 由于条件不满足，消费阻塞  
                    list.wait();  
                }  
                catch (InterruptedException e)  
                {  
                    e.printStackTrace();  
                }  
            }  
            sf=list.get(0);
            // 消费条件满足情况下，消费num个产品  
            //user.addPool(sf);
            System.err.println("		★消费一个 【现仓储量为】:" + list.size()); 
            list.remove();  
            list.notifyAll();  
            return sf; 
        }  
    }  
  
    // get/set方法  
    public LinkedList<SfSearch> getList()  
    {  
        return list;  
    }  
  
    public void setList(LinkedList<SfSearch> list)  
    {  
        this.list = list;  
    }  
  
    public int getMAX_SIZE()  
    {  
        return MAX_SIZE;  
    }  
}  