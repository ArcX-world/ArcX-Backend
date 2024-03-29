package avatar.util.innoMsg;

import com.alipay.api.java_websocket.client.WebSocketClient;
import com.alipay.api.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 同步自研设备客户端
 */
public class SyncInnoClient extends WebSocketClient {
 public SyncInnoClient(String url) throws URISyntaxException {
  super(new URI(url));
 }

 @Override
 public void onOpen(ServerHandshake serverHandshake) {
  //连接成功处理
  SyncInnoDealUtil.socketOpen(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onMessage(String s) {
  //接收到信息处理
  SyncInnoUtil.dealMsg(s);
 }

 @Override
 public void onClose(int i, String s, boolean b) {
  //关闭连接处理
  SyncInnoDealUtil.socketClose(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onError(Exception e) {
  //连接错误处理
  SyncInnoDealUtil.socketError(this.getURI().getHost(), this.getURI().getPort());
 }

}
