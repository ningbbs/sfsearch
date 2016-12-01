package ningbbs.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ningbbs.data.info.User;
import ningbbs.dydh.util.SendTimeUtil;
import ningbbs.service.SfDbService;
import ningbbs.service.SfScanService;
import ningbbs.util.Constants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userNameField;
	private JTextField passWordField;
	public static User user;
	private JTextField textField;
	private JScrollPane scrollPane;
	public static JTextArea textArea;
	public static JLabel imageCodeLabel;
	private JTextField textField_2;
	private JButton button;
	private JLabel lblNewLabel;
	private JTextField textField_3;
	private JTextField textField_4;
	private JLabel label_2;
	private JLabel lblNewLabel_1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui frame = new MainGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainGui() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				saveProperties();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 637, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		JLabel label = new JLabel("\u7528\u6237\u540D");
		contentPane.add(label, "2, 2, right, default");

		userNameField = new JTextField();
		userNameField.setText("wangmin");
		contentPane.add(userNameField, "4, 2, fill, default");
		userNameField.setColumns(10);

		JLabel label_1 = new JLabel("\u5BC6\u7801");
		contentPane.add(label_1, "6, 2, right, default");

		passWordField = new JTextField();
		passWordField.setText("bctv041");
		contentPane.add(passWordField, "8, 2, fill, default");
		passWordField.setColumns(10);

		final JLabel imageCodeLabel = new JLabel("imageCode");
		contentPane.add(imageCodeLabel, "10, 2");

		JButton btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();  
				for(int i=0;i<Constants.roundNum;i++){
					singleThreadExecutor.execute(new Runnable() {
						public void run() {
							autoPlay();
						}
					});
				}
			}
		});

		label_2 = new JLabel("\u6BB5");
		contentPane.add(label_2, "2, 4, right, default");

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setText("606641");
		contentPane.add(textField, "4, 4, fill, default");
		textField.setColumns(10);

		lblNewLabel = new JLabel("\u52A0\u51E0\u6BB5\u53D1");
		contentPane.add(lblNewLabel, "6, 4, right, default");

		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setText("2");
		contentPane.add(textField_3, "8, 4, fill, default");
		textField_3.setColumns(10);
		contentPane.add(btnSend, "10, 4");

		button = new JButton("\u4FEE\u6539\u65F6\u95F4");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Constants.SEND_HOUR1 = Integer.parseInt(textField_2.getText());
			}
		});
		contentPane.add(button, "2, 6");

		textField_2 = new JTextField();
		textField_2.setText("8");
		contentPane.add(textField_2, "4, 6, fill, default");
		textField_2.setColumns(10);

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user = new User(userNameField.getText(), passWordField
						.getText());
				try {
					user.login(imageCodeLabel);
					System.out.println(user.getMoney());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		lblNewLabel_1 = new JLabel("\u5206\u7EBF\u7A0B");
		contentPane.add(lblNewLabel_1, "6, 6, right, default");

		textField_4 = new JTextField();
		textField_4.setText("5");
		contentPane.add(textField_4, "8, 6, fill, default");
		textField_4.setColumns(10);
		contentPane.add(loginButton, "10, 6");

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "2, 8, 9, 1, fill, fill");

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("黑体", Font.BOLD, 16));
		scrollPane.setViewportView(textArea);
		loadProperties();
		new Thread(new SendTimeUtil(600000)).start();
	}

	protected void autoPlay() {
		String txt=JOptionPane.showInputDialog("是否修改段", textField.getText());
		int startIndex=0;
		try {
			startIndex = Integer.parseInt(JOptionPane.showInputDialog("是否修改发送启始索引", startIndex));
		} catch (Exception e) {
		}
		if(txt!=null){
			textField.setText(txt);
		}
		SfScanService scan=Constants.ctx.getBean(SfScanService.class);
		SfDbService db=Constants.ctx.getBean(SfDbService.class);
		scan.initParams(txt, 0);
		for(int i=0;i<100;i++)
		db.add_kd_DB(scan.scan(scan));
	}
	

	private void saveProperties() {
		try {
			System.out.println("save");
			if (!new File(Constants.configFile).exists()) {
				System.out.println(Constants.configFile + "文件不存在");
				return;
			}
			OutputStream out = new FileOutputStream(new File(
					Constants.configFile));
			Properties pps = new Properties();
			pps.setProperty("order", textField.getText());
			pps.setProperty("sendHour1", textField_2.getText());
			pps.setProperty("thread", textField_3.getText());
			pps.setProperty("splistSize", textField_4.getText());
			pps.setProperty("threadSize", Constants.threadSize + "");
			pps.setProperty("sendHour2", Constants.SEND_HOUR2 + "");
			pps.setProperty("sendSleepTime", Constants.sendSleepTime + "");
			pps.setProperty("roundNum", Constants.roundNum + "");
			pps.setProperty("scanSleepTime", Constants.scanSleepTime + "");
			pps.setProperty("typeCodeStr", Constants.typeCodeStr);
			pps.store(out, "#");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadProperties() {
		try {
			Properties prop = new Properties();// 属性集合对象
			FileInputStream fis = new FileInputStream(Constants.configFile);// 属性文件流
			prop.load(fis);// 将属性文件流装载到Properties对象中
			Constants.SEND_HOUR1 = Integer.parseInt(prop
					.getProperty("sendHour1"));
			Constants.SEND_HOUR2 = Integer.parseInt(prop
					.getProperty("sendHour2"));
			Constants.sendSleepTime = Integer.parseInt(prop
					.getProperty("sendSleepTime"));
			Constants.roundNum = Integer.parseInt(prop.getProperty("roundNum"));
			Constants.thread = Integer.parseInt(prop.getProperty("thread"));
			Constants.threadSize = Integer.parseInt(prop
					.getProperty("threadSize"));
			Constants.splistSize = Integer.parseInt(prop
					.getProperty("splistSize"));
			Constants.scanSleepTime = Integer.parseInt(prop
					.getProperty("scanSleepTime"));
			Constants.typeCodeStr = prop.getProperty("typeCodeStr");
			Constants.TypeCodeArr = Constants.typeCodeStr.split(",");
			textField.setText(prop.getProperty("order"));
			textField_2.setText(Constants.SEND_HOUR1 + "");
			textField_3.setText(Constants.thread + "");
			textField_4.setText(Constants.splistSize + "");

			Constants
					.sendMsg("读取配置文件-循环次数: " + Constants.roundNum, true, false);
			Constants.sendMsg("读取配置文件-线程发送间隔: " + Constants.scanSleepTime,
					true, false);
			Constants.sendMsg("读取配置文件-异地发送小时间隔: " + Constants.SEND_HOUR2, true,
					false);
			Constants.sendMsg("读取配置文件-类型: " + Constants.typeCodeStr, true,
					false);
			Constants.sendMsg("缓冲睡眠数量: " + Constants.threadSize, true, false);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
