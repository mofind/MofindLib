package mofind.net;
//package mofind.utils;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.PacketCollector;
//import org.jivesoftware.smack.Roster;
//import org.jivesoftware.smack.RosterEntry;
//import org.jivesoftware.smack.RosterGroup;
//import org.jivesoftware.smack.SmackConfiguration;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.filter.AndFilter;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.filter.PacketIDFilter;
//import org.jivesoftware.smack.filter.PacketTypeFilter;
//import org.jivesoftware.smack.packet.IQ;
//import org.jivesoftware.smack.packet.Packet;
//import org.jivesoftware.smack.packet.Presence;
//import org.jivesoftware.smack.packet.Registration;
//import org.jivesoftware.smack.provider.ProviderManager;
//import org.jivesoftware.smack.util.StringUtils;
//import org.jivesoftware.smackx.Form;
//import org.jivesoftware.smackx.ReportedData;
//import org.jivesoftware.smackx.ReportedData.Row;
//import org.jivesoftware.smackx.packet.VCard;
//import org.jivesoftware.smackx.search.UserSearchManager;
//
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//
//
//public class XMPPUtils {
//	public static XMPPConnection connection;
//
//	public static int openconnect() {
////		ConnectionConfiguration config = new ConnectionConfiguration(
////				Constant.SERVER_HOST_IP, Constant.SERVER_CLIENT_PORT,Constant.SERVER_HOST);
//		ConnectionConfiguration config = new ConnectionConfiguration(Constant.SERVER_HOST_IP, Constant.SERVER_CLIENT_PORT, Constant.SERVER_HOST);
//		
//		 /** 是否启用安全验证 */  
////        config.setSASLAuthenticationEnabled(false);  
//        /** 是否启用调试 */  
//        //config.setDebuggerEnabled(true);   
//        /** 创建connection链接 */ 
//		try {
//			connection = new XMPPConnection(config);
//			connection.connect();
//			return Constant.CONNECTION_OK;
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
//		return Constant.CONNECTION_FAIL;
//	}
//
//	public static boolean closeconnect() {
//		if (connection != null) {
//			connection.disconnect();
//			connection = null;
//		}
//		return true;
//	}
//
//	public static XMPPConnection getconnection() {
//		if (connection == null) {
//			openconnect();
//		}
//		return connection;
//	}
//	/**
//	 * 注册
//	 * 
//	 * @param account 注册帐号
//	 * @param password 注册密码
//	 * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
//	 */
//	public static int registe(User user) {
//		if (connection == null) XMPPTool.getconnection();
//		Registration regist = new Registration();
//		regist.setType(IQ.Type.SET);
//		regist.setTo(connection.getServiceName());
//		regist.setUsername(user.getUsername());// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
//		regist.setPassword(user.getUserpassword());
//		regist.addAttribute("android", "geolo_createUser_android");// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
//		PacketFilter filter = new AndFilter(new PacketIDFilter(
//				regist.getPacketID()), new PacketTypeFilter(IQ.class));
//		PacketCollector collector = connection.createPacketCollector(filter);
//		connection.sendPacket(regist);
//		IQ result = (IQ) collector.nextResult(SmackConfiguration
//				.getPacketReplyTimeout());
//		// Stop queuing results
//		collector.cancel();// 停止请求results（是否成功的结果）
//		if (result == null) {
//			Log.e("RegistActivity", "No response from server.");
//			return Constant.REGIST_NORESULT;
//		} else if (result.getType() == IQ.Type.RESULT) {
//			return Constant.REGIST_OK;
//		} else { // if (result.getType() == IQ.Type.ERROR)
//			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
//				Log.e("RegistActivity", "IQ.Type.ERROR: "
//						+ result.getError().toString());
//				return Constant.REGIST_EXIST;
//			} else {
//				Log.e("RegistActivity", "IQ.Type.ERROR: "
//						+ result.getError().toString());
//				return Constant.REGIST_FAIL;
//			}
//		}
//	}
//	
//	/**
//	 * 登录
//	 * 
//	 * @param a 登录帐号
//	 * @param p 登录密码
//	 * @return 0 无连接，1成功，2登录超时
//	 */
//	public static int login(User user) {
//		try {
//			if (connection == null)
//				connection = getconnection();
//			
//			if(connection == null)
//			{
//				return Constant.LOGIN_NOCONNECTION;
//			}
//			/** 登录 */
//			connection.login(user.getUsername(), user.getUsername());
//			
//			Presence presence = new Presence(Presence.Type.available);
//			XMPPTool.getconnection().sendPacket(presence);
//			if(presence.isAvailable())
//			{
//				return Constant.LOGIN_SUCCEED;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			XMPPTool.closeconnect();
//		}
//		return Constant.LOGIN_FAIL;
//	}
//	
//	/**  
//	 * 修改密码  
//	 * @param connection  
//	 * @return  
//	 */    
//	public static boolean changePassword(String changpassword)    
//	{    
//	    try {    
//	        connection.getAccountManager().changePassword(changpassword);    
//	        return true;    
//	    } catch (Exception e) {    
//	        return false;    
//	    }    
//	}   
//	
//	/** 
//	 * 更改用户状态 
//	 * 0 在线，1  在聊天，2忙碌，3离开，4隐身，5离线
//	 * 
//	 */  
//	public static void setPresence(int state) {
//	    if (connection == null)  
//	        XMPPTool.getconnection();  
//	    Presence presence;  
//	    switch (state) {  
//	        case 0:  
//	            presence = new Presence(Presence.Type.available);  
//	            connection.sendPacket(presence);  
//	            Log.v("state", "设置在线");  
//	            break;  
//	        case 1:  
//	            presence = new Presence(Presence.Type.available);  
//	            presence.setMode(Presence.Mode.chat);  
//	            connection.sendPacket(presence);  
//	            Log.v("state", "设置Q我吧");  
//	            System.out.println(presence.toXML());  
//	            break;  
//	        case 2:  
//	            presence = new Presence(Presence.Type.available);  
//	            presence.setMode(Presence.Mode.dnd);  
//	            connection.sendPacket(presence);  
//	            Log.v("state", "设置忙碌");  
//	            System.out.println(presence.toXML());  
//	            break;  
//	        case 3:  
//	            presence = new Presence(Presence.Type.available);  
//	            presence.setMode(Presence.Mode.away);  
//	            connection.sendPacket(presence);  
//	            Log.v("state", "设置离开");  
//	            System.out.println(presence.toXML());  
//	            break;  
//	        case 4:  
//	            Roster roster = connection.getRoster();  
//	            Collection<RosterEntry> entries = roster.getEntries();  
//	            for (RosterEntry entry : entries) {  
//	                presence = new Presence(Presence.Type.unavailable);  
//	                presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
//	                presence.setFrom(connection.getUser());  
//	                presence.setTo(entry.getUser());  
//	                connection.sendPacket(presence);  
//	                System.out.println(presence.toXML());  
//	            }  
//	            // 向同一用户的其他客户端发送隐身状态   
//	            presence = new Presence(Presence.Type.unavailable);  
//	            presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
//	            presence.setFrom(connection.getUser());  
//	            presence.setTo(StringUtils.parseBareAddress(connection.getUser()));  
//	            connection.sendPacket(presence);  
//	            Log.v("state", "设置隐身");  
//	            break;  
//	        case 5:  
//	            presence = new Presence(Presence.Type.unavailable);  
//	            connection.sendPacket(presence);  
//	            Log.v("state", "设置离线");  
//	            break;  
//	        default:  
//	            break;  
//	        }  
//	    }  
//	
//	/** 
//     * 获取用户头像信息 
//     *  
//     * @param connection 
//     * @param user 
//     * @return 
//     */  
//    public static Drawable getUserImage(User user) {  
//        ByteArrayInputStream bais = null;  
//        try {  
//            // 加入这句代码，解决No VCard for  
//            ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",  
//                    new org.jivesoftware.smackx.provider.VCardProvider());  
//  
//            user.getUservcard().load(connection, user.getUserJID());  
//  
//            if (user.getUservcard() == null || user.getUservcard().getAvatar() == null)  
//                return null;  
//            bais = new ByteArrayInputStream(user.getUservcard().getAvatar());  
//  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        if (bais == null)  
//            return null;  
//        return FormatTool.getInstance().InputStream2Drawable(bais);  
//    }  
//	/** 
//     * 修改用户头像 
//     *  
//     * @param connection 
//     * @param file 
//     * @throws XMPPException 
//     * @throws IOException 
//     */  
//    public static void changeImage(File file,User user)throws XMPPException, IOException {  
//  
//    	user.getUservcard().load(connection);  
//  
//        byte[] bytes;  
//  
//        bytes = getFileBytes(file);  
//        String encodedImage = StringUtils.encodeBase64(bytes);  
//        user.getUservcard().setAvatar(bytes, encodedImage);  
//        user.getUservcard().setEncodedImage(encodedImage);  
//        user.getUservcard().setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>" + encodedImage  
//                + "</BINVAL>", true);  
//  
//        ByteArrayInputStream bais = new ByteArrayInputStream(user.getUservcard().getAvatar());  
//        FormatTool.getInstance().InputStream2Bitmap(bais);  
//  
//        user.getUservcard().save(connection);  
//    }  
//	 @SuppressWarnings("resource")
//	private static byte[] getFileBytes(File file) {
//		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			FileInputStream fis = null;
//			try {
//				 fis = new FileInputStream(file);
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			}
//			byte[] buffer = new byte[1024];
//			int size;
//			try {
//				while ((size = fis.read(buffer, 0, 1024)) != -1) {
//					bos.write(buffer, 0, size);
//				}
//				byte[] content = bos.toByteArray();
//				bos.close();
//				return content;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return null;
//			}
//	}
//
//	/**
//	  * 获取用户VCard信息
//	  *
//	  * @param connection
//	  * @param username
//	  * @return
//	  * @throws XMPPException
//	  */
//	 public static VCard getUserVCard(User user)throws XMPPException {
//		  VCard vcard = new VCard();
//		  vcard.load(connection, user.getUsername());
//		  return vcard;
//	 }
//	 
//	 /**
//	  * 获取所有组
//	  *
//	  * @param roster
//	  * @return 所有组集合
//	  */
//	 public static List<RosterGroup> getGroups() {
//		  Roster roster = connection.getRoster();	 
//		  List<RosterGroup> grouplist = new ArrayList<RosterGroup>();
//		  Collection<RosterGroup> rosterGroup = roster.getGroups();
//		  Iterator<RosterGroup> i = rosterGroup.iterator();
//		  while (i.hasNext()) {
//		   grouplist.add(i.next());
//		  }
//		  return grouplist;
//	 }
//	 /**
//	  * 获取某个组里面的所有好友
//	  *
//	  * @param roster
//	  * @param groupName
//	  *            组名
//	  * @return
//	  */
//	 public static List<RosterEntry> getEntriesByGroup(String groupName) {
//		  Roster roster = connection.getRoster();
//		  List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
//		  RosterGroup rosterGroup = roster.getGroup(groupName);
//		  Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
//		  Iterator<RosterEntry> i = rosterEntry.iterator();
//		  while (i.hasNext()) {
//		   Entrieslist.add(i.next());
//		  }
//		  return Entrieslist;
//	 }
//	 public static List<String> getFriendNamesByGroup(String groupNmae)
//	 {
//		 List<String> list_friendnames = new ArrayList<String>();
//		 List<RosterEntry> list = new ArrayList<RosterEntry>();
//		 list = getEntriesByGroup(groupNmae);
//		 for(RosterEntry item : list)
//		 {
//			 list_friendnames.add(item.getUser().split("@")[0]);
//		 }
//		 return list_friendnames;
//	 }
//	 public static List<Friend> getFriendsByGroup(String groupNmae)
//	 {
//		 List<Friend> list_friendnames = new ArrayList<Friend>();
//		 List<RosterEntry> list = new ArrayList<RosterEntry>();
//		 list = getEntriesByGroup(groupNmae);
//		 for(RosterEntry item : list)
//		 {
//			 list_friendnames.add(new Friend(item.getUser(),item.getUser().split("@")[0],null));
//		 }
//		 return list_friendnames;
//	 }
//	 /**
//	  * 获取所有好友信息
//	  *
//	  * @param roster
//	  * @return
//	  */
//	 public static List<RosterEntry> getAllEntries() {
//		 if(connection == null)
//		 {
//			 connection = getconnection();
//		 }
//		 Roster roster = connection.getRoster();
//		 List<RosterEntry> Entrieslist = new ArrayList<RosterEntry>();
//		 Collection<RosterEntry> rosterEntry = roster.getEntries();
//		 Iterator<RosterEntry> i = rosterEntry.iterator();
//		 while (i.hasNext()) {
//			 Entrieslist.add(i.next());
//		 }
//		  return Entrieslist;
//	 }
//	 public static List<String> getAllFriendNames()
//	 {
//		 List<String> list_allfriendnames = new ArrayList<String>();
//		 List<RosterEntry> list = new ArrayList<RosterEntry>();
//		 list = getAllEntries();
//		 for(RosterEntry item : list)
//		 {
//			 list_allfriendnames.add(item.getUser().split("@")[0]);
//		 }
//		 return list_allfriendnames;
//	 }
//	 public static List<Friend> getAllFriends()
//	 {
//		 List<Friend> list_allfriends = new ArrayList<Friend>();
//		 List<RosterEntry> list = new ArrayList<RosterEntry>();
//		 list = getAllEntries();
//		 for(RosterEntry item : list)
//		 {
//			 list_allfriends.add(new Friend(item.getUser(),item.getUser().split("@")[0], null));
//		 }
//		 return list_allfriends;
//	 }
//	 /**
//     * 添加好友 有分组
//     * 
//     * @param roster
//     * @param username
//     * @param nickname
//     * @param groupName
//     * @return
//     */ 
//    public static boolean addUser(String groupName,Friend friend) { 
//    	Roster roster = connection.getRoster();
//        try { 
//            roster.createEntry(friend.getFriendusername()+"@"+Constant.SERVER_NAME, friend.getFriendusername(), new String[] { groupName }); 
//            return true; 
//        } catch (Exception e) { 
//            e.printStackTrace(); 
//            return false; 
//        } 
//    } 
//
//	 /**
//	  * 添加好友 无分组
//	  *
//	  * @param roster
//	  * @param user
//	  * @param nickname
//	  * @return
//	  */
//	 public static boolean addUser(Friend friend) {
//		   Roster roster = connection.getRoster();
//		  try {
//		   roster.createEntry(friend.getFriendJID(), friend.getFriendnickname(), null);
//		   return true;
//		  } catch (Exception e) {
//		   e.printStackTrace();
//		   return false;
//		  }
//	 }
//	 /**
//	  * 删除好友
//	  *
//	  * @param roster
//	  * @param username
//	  * @return
//	  */
//	 public static boolean removeUser(Friend friend) {
//		  Roster roster = connection.getRoster();
//		  String friendname = null;
//		  try {
////		   if (friend.getFriendJID().contains("@")) {
//		    friendname = friend.getFriendJID();
////		   }
//	
//		   RosterEntry entry = roster.getEntry(friendname);
//		   System.out.println("删除好友：" + friendname);
//		   System.out.println("User." + roster.getEntry(friendname) == null);
//		   roster.removeEntry(entry);
//		   return true;
//		  } catch (Exception e) {
//		   e.printStackTrace();
//		   return false;
//		  }
//	 }
//	 /** 
//     * 查询用户 
//     *  
//     * @param connection 
//     * @param serverDomain 
//     * @param userName 
//     * @return 
//     * @throws XMPPException 
//     */  
//    public static List<Friend> searchUsers(String userName) throws XMPPException {  
//        List<Friend> results = new ArrayList<Friend>();  
//        System.out.println("查询开始..............." + connection.getHost()  
//                + connection.getServiceName());  
//  
//        UserSearchManager usm = new UserSearchManager(connection);  
//  
//        Form searchForm = usm.getSearchForm(connection.getServiceName());  
//        Form answerForm = searchForm.createAnswerForm();  
//        answerForm.setAnswer("userAccount", true);  
//        answerForm.setAnswer("userPhote", userName);  
//        ReportedData data = usm.getSearchResults(answerForm, connection.getServiceName());  
//  
//        Iterator<Row> it = data.getRows();  
//        Row row = null;  
//        Friend friend = null;  
//        while (it.hasNext()) {  
//            friend = new Friend();  
//            row = it.next();  
//            friend.setFriendJID(row.getValues("userAccount").next().toString());  
//            friend.setFriendusername(row.getValues("userPhote").next().toString());  
//  
//            System.out.println(row.getValues("userAccount").next());  
//            System.out.println(row.getValues("userPhote").next());  
//            results.add(friend);  
//            // 若存在，则有返回,UserName一定非空，其他两个若是有设，一定非空   
//        }  
//        return results;  
//    }  
//
//	 
//	/**  
//	 * 注销当前用户  
//	 * @param connection  
//	 * @return  
//	 */    
//	public static boolean deleteAccount()    
//	{    
//	    try {    
//	        connection.getAccountManager().deleteAccount();    
//	        return true;    
//	    } catch (Exception e) {    
//	        return false;    
//	    }    
//	}    
//	/**
//	 * 将输入流的内容读入到一个byte数组
//	 * 
//	 * @param is
//	 *            输入流
//	 * @return 输入流内容构成的byte[]
//	 */
//	@SuppressWarnings("resource")
//	public byte[] readAllStream(File file) {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		FileInputStream fis = null;
//		byte[] buffer = new byte[1024];
//		int size;
//		try {
//			
//			 fis = new FileInputStream(file);
//			 while ((size = fis.read(buffer, 0, 1024)) != -1) {
//					bos.write(buffer, 0, size);
//				}
//				byte[] content = bos.toByteArray();
//				bos.close();
//				return content;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}		
//	}
//}
