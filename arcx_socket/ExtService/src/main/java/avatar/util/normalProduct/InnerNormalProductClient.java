package avatar.util.normalProduct;

import com.alipay.api.java_websocket.client.WebSocketClient;
import com.alipay.api.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 内部客户端
 */
public class InnerNormalProductClient extends WebSocketClient {
 public InnerNormalProductClient(String url) throws URISyntaxException {
  super(new URI(url));
 }

 @Override
 public void onOpen(ServerHandshake serverHandshake) {
  //连接成功处理
  InnerNormalProductDealUtil.socketOpen(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onMessage(String s) {
  //接收到信息处理
  InnerNormalProductUtil.dealMsg(s);
 }

 @Override
 public void onClose(int i, String s, boolean b) {
  //关闭连接处理
  InnerNormalProductDealUtil.socketClose(this.getURI().getHost(), this.getURI().getPort());
 }

 @Override
 public void onError(Exception e) {
  //连接错误处理
  InnerNormalProductDealUtil.socketError(this.getURI().getHost(), this.getURI().getPort());
 }

}
