package cn.hackzone.netty.websocket.web;

import cn.hackzone.netty.websocket.service.PushMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推送消息控制器
 *
 * @author maxwell
 * @date 2023/03/10
 */
@RestController
public class PushMsgController {

    private final PushMsgService pushMsgService;

    public PushMsgController(PushMsgService pushMsgService) {
        this.pushMsgService = pushMsgService;
    }

    @PostMapping("/push/to/all")
    public ResponseEntity<Boolean> pushMsgToAll(@RequestParam("msg") String msg) {
        pushMsgService.pushMsgToAll(msg);
        return ResponseEntity.ok(Boolean.TRUE);
    }


    @PostMapping("/push")
    public ResponseEntity<Boolean> pushMsg(@RequestParam("username") String username,
                                           @RequestParam("msg") String msg) {
        pushMsgService.pushMsg(username, msg);
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
