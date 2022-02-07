package com.xinxin.BotTool;

import com.xinxin.BotEvent.GroupMessageEvent;
import com.xinxin.BotEvent.NoticeEvent;
import com.xinxin.BotEvent.PrivateMessageEvent;
import com.xinxin.BotEvent.RequestEvent;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class cqHttpBot {
	private String Json;//消息原本文本
	private String message_type;//消息类型

	private String self_id;//机器人qq
	private String message_id;//消息ID、被撤回的消息ID
	private String raw_message;//收到消息
	private String group_id;//消息群号
	private String nickname;//发送人昵称
	private String user_id;//发送人qq、撤回消息qq、事件触发qq
	private String sub_type;//事件子类型

	private String post_type;//上报类型
	private String notice_type;//通知类型
	private String operator_id;//事件操作者QQ
	private String duration;//被禁言时长
	private String file_name;//上传文件名字
	private String file_size;//上传文件大小
	private String target_id;//被戳、运气王QQ

	private String request_type;//请求类型
	private String comment;//验证消息
	private String flag;//请求flag

	private String temp_source;//临时消息来源
	private String font;//字体

	public cqHttpBot(String jsonStr, JSONObject json){

		this.Json = jsonStr;//消息原本json文本

		if(Json.contains("post_type")){
			if(json.getString("post_type").equalsIgnoreCase("meta_event")){
				return;
			}
			post_type = json.getString("post_type");
		}else {
			post_type = null;
		}
		if(Json.contains("message_type")){
			message_type = json.getString("message_type");
		}else {
			message_type = null;
		}

		if(message_type != null){
			//群聊事件
			if(message_type.equalsIgnoreCase("group")){
				this.self_id = json.getString("self_id");//机器人qq
				this.message_id = json.getString("message_id");//消息ID、被撤回的消息ID
				this.raw_message = json.getString("raw_message");//收到消息
				this.group_id = json.getString("group_id");//消息群号
				this.nickname = JSONObject.fromObject(json.getString("sender")).getString("nickname");//发送人昵称
				this.user_id = json.getString("user_id");//发送人qq
				this.sub_type = json.getString("sub_type");//事件子类型

				GroupMessageEvent event = new GroupMessageEvent(Json,self_id,message_id,raw_message,group_id,nickname,user_id,sub_type);
				Bukkit.getPluginManager().callEvent(event);
			}

			//私聊事件
			if(message_type.equalsIgnoreCase("private")){
				this.self_id = json.getString("self_id");//机器人qq
				this.raw_message = json.getString("raw_message");//收到消息
				this.nickname = JSONObject.fromObject(json.getString("sender")).getString("nickname");//发送人昵称
				this.user_id = json.getString("user_id");//发送人qq
				this.sub_type = json.getString("sub_type");//事件子类型

				if(Json.contains("group_id")){
					this.temp_source = JSONObject.fromObject(json.getString("sender")).getString("group_id");//临时消息来源
				}else {
					this.temp_source = null;
				}

				this.message_id = json.getString("message_id");//消息id

				PrivateMessageEvent event = new PrivateMessageEvent(Json,self_id,raw_message,nickname,user_id,sub_type,temp_source,message_id);
				Bukkit.getPluginManager().callEvent(event);
			}
		}


		if(post_type != null){
			//通知事件
			if(post_type.equalsIgnoreCase("notice")){
				this.notice_type = json.getString("notice_type");//通知类型

				if(Json.contains("self_id")){
					this.self_id = json.getString("self_id");//机器人qq
				}else {
					this.self_id = null;
				}
				if(Json.contains("notice_type")){
					this.notice_type = json.getString("notice_type");//通知类型
				}else {
					this.notice_type = null;
				}
				if(Json.contains("sub_type")){
					this.sub_type = json.getString("sub_type");//事件子类型
				}else {
					this.sub_type = null;
				}
				if(Json.contains("user_id")){
					this.user_id = json.getString("user_id");//发送人qq、撤回消息qq、事件触发qq
				}else {
					this.user_id = null;
				}
				if(Json.contains("operator_id")){
					this.operator_id = json.getString("operator_id");//操作者QQ
				}else {
					this.operator_id = null;
				}
				if(Json.contains("duration")){
					this.duration = json.getString("duration");//被禁言时长
				}else {
					this.duration = null;
				}
				if(Json.contains("message_id")){//消息ID、被撤回的消息ID
					this.message_id = json.getString("message_id");
				}else {
					this.message_id = null;
				}
				if(Json.contains("file")){//上传文件数据
					this.file_name = JSONObject.fromObject(json.getString("file")).getString("name");//上传文件名字
					this.file_size = JSONObject.fromObject(json.getString("file")).getString("size");//上传文件大小
				}else {
					this.file_name = null;
					this.file_size = null;
				}
				if(Json.contains("target_id")){//被戳、运气王QQ
					this.target_id = json.getString("target_id");
				}else {
					this.target_id = null;
				}

				NoticeEvent event = new NoticeEvent(Json,self_id,sub_type,notice_type,operator_id,duration,message_id,file_name,file_size,target_id);
				Bukkit.getPluginManager().callEvent(event);
			}

			//请求事件|加群、邀请机器人加群、加机器人好友
			if(post_type.equalsIgnoreCase("request")){
				this.self_id = json.getString("self_id");//机器人qq
				this.user_id = json.getString("user_id");//发送请求的qq
				this.request_type = json.getString("request_type");//请求类型
				this.comment = json.getString("comment");//验证消息
				this.flag = json.getString("flag");//请求flag

				if(Json.contains("group_id")){//请求群号
					this.group_id = json.getString("group_id");
				}else {
					this.group_id = null;
				}
				if(Json.contains("sub_type")){//请求子类型
					this.sub_type = json.getString("sub_type");
				}else {
					this.sub_type = null;
				}

				RequestEvent event = new RequestEvent(Json,self_id,user_id,request_type,comment,flag,group_id,sub_type);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
	}

}
