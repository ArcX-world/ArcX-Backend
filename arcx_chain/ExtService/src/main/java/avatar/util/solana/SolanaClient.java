package avatar.util.solana;

import com.alipay.api.java_websocket.client.WebSocketClient;
import com.alipay.api.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * solana设备客户端
 */
public class SolanaClient extends WebSocketClient {
 public SolanaClient(String url) throws URISyntaxException {
  super(new URI(url));
 }

 @Override
 public void onOpen(ServerHandshake serverHandshake) {
  //连接成功处理
  SolanaDealUtil.socketOpen(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onMessage(String s) {
  //接收到信息处理
  SolanaDealUtil.dealMsg(s);
 }

 @Override
 public void onClose(int i, String s, boolean b) {
  //关闭连接处理
  SolanaDealUtil.socketClose();
 }

 @Override
 public void onError(Exception e) {
  //连接错误处理
  SolanaDealUtil.socketError(this.getURI().getHost(), this.getURI().getPort());
 }

}
