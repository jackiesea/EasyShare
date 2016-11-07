package cn.joy.libs.platform.wechat;

import android.os.AsyncTask;

import cn.joy.libs.platform.ShareParams;
import cn.joy.libs.platform.ShareWithReceiver;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * User: JiYu
 * Date: 2016-07-28
 * Time: 18-22
 *
 */

abstract class WechatShareBase extends ShareWithReceiver<Wechat> {

	WechatShareBase(Wechat platform, ShareParams params) {
		super(platform, params);
	}

	WechatShareBase(Wechat platform, ShareParams params, boolean shareByClient) {
		super(platform, params, shareByClient);
	}

	@Override
	protected boolean checkInstall() {
		return getPlatform().checkInstall();
	}

	@Override
	protected boolean checkArgs() {
		return true;
	}

	@Override
	public boolean share() {
		if (!super.share())
			return false;
		new AsyncTask<Void, Void, SendMessageToWX.Req>() {
			@Override
			protected SendMessageToWX.Req doInBackground(Void... voids) {
				SendMessageToWX.Req req = null;
				switch (getShareParams().getShareType()) {
					case Text:
						req = new WeChatShareTextMessage(getShareParams(), getScene()).createMessage();
						break;
					case TextAndImage:
						req = new WeChatShareImageAndTextMessage(getShareParams(), getScene()).createMessage();
						break;
				}
				return req;
			}

			@Override
			protected void onPostExecute(SendMessageToWX.Req req) {
				super.onPostExecute(req);
				if (req != null)
					getPlatform().getApi().sendReq(req);
			}
		}.execute();
		return true;
	}

	public abstract int getScene();
}