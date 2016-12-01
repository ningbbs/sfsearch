package ningbbs.storage;

import java.util.LinkedList;  

import ningbbs.data.info.User;
import ningbbs.kd.SfSearch;
import ningbbs.service.SfScanService;

/** 
 * �ֿ���Storageʵ�ֻ����� 
 *  
 */  
public class Storage  
{  
    // �ֿ����洢��  
    private final int MAX_SIZE = 10;  
    // �ֿ�洢������  
    private LinkedList<SfSearch> list = new LinkedList<SfSearch>();  //�ɸ���Ϊ��������
    // ����num����Ʒ  
    public void produce(SfScanService scanService) throws Exception 
    {  
        // ͬ�������  
        synchronized (list)  
        {  
            // ����ֿ�ʣ����������  
            while (list.size() > MAX_SIZE)  
            {  
               System.out.println("���������:"   + list.size() + "\t��ʱ����ִ����������!");  
                try  
                {  
                    // �������������㣬��������  
                    list.wait();  
                }  
                catch (InterruptedException e)  
                {  
                    e.printStackTrace();  
                }  
            }
            System.err.println("		������һ�� ��������ִ���Ϊ��:" + list.size());  
            list.addAll(scanService.scan(scanService)); //����
            list.notifyAll();  
        }  
    }  
  
    // ����num����Ʒ  
    public SfSearch consume(User user)  throws Exception 
    {  
    	SfSearch sf=null;
        // ͬ�������  
        synchronized (list)  
        {  
            // ����ֿ�洢������  
            while (list.size() < 1)  
            {  
                System.out.println("���������:"   + list.size() + "\t��ʱ����ִ����������!");  
                try  
                {  
                    // �������������㣬��������  
                    list.wait();  
                }  
                catch (InterruptedException e)  
                {  
                    e.printStackTrace();  
                }  
            }  
            sf=list.get(0);
            // ����������������£�����num����Ʒ  
            //user.addPool(sf);
            System.err.println("		������һ�� ���ֲִ���Ϊ��:" + list.size()); 
            list.remove();  
            list.notifyAll();  
            return sf; 
        }  
    }  
  
    // get/set����  
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