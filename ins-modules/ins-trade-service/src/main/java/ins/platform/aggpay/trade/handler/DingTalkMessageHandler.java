/*
 * Copyright (c) 2018-2020, Ripin Yan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ins.platform.aggpay.trade.handler;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import ins.platform.aggpay.common.util.template.DingTalkMsgTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lengleng
 * @date 2018/4/22
 * 发送钉钉消息逻辑
 */
@Slf4j
@Component
public class DingTalkMessageHandler {

    /**
     * 业务处理
     *
     * @param text 消息
     */
    public boolean process(String text) {
        String webhook = "https://oapi.dingtalk.com/robot/send?access_token=60f966f9ae7c7e73c629a46aca84c83517fc8196cd724c4688c906be3f029feb";
        if (StrUtil.isBlank(webhook)) {
            log.error("钉钉配置错误，webhook为空");
            return false;
        }

        DingTalkMsgTemplate dingTalkMsgTemplate = new DingTalkMsgTemplate();
        dingTalkMsgTemplate.setMsgtype("text");
        DingTalkMsgTemplate.TextBean textBean = new DingTalkMsgTemplate.TextBean();
        textBean.setContent(text);
        dingTalkMsgTemplate.setText(textBean);
        String result = HttpUtil.post(webhook, JSONObject.toJSONString(dingTalkMsgTemplate));
        log.info("钉钉提醒测试成功,报文响应:{}", result);
        return true;
    }

    public static void main(String[] args) {
        String webhook = "https://oapi.dingtalk.com/robot/send?access_token=60f966f9ae7c7e73c629a46aca84c83517fc8196cd724c4688c906be3f029feb";

        String result = HttpUtil.post(webhook, "{\n" + "     \"msgtype\": \"text\",\n" + "     \"text\": {\n" + "         \"content\": \"@17688917488 晚上请客吃大餐\"\n" + "     },\n" + "     \"at\": {\n" + "         \"atMobiles\": [\n" + "             \"17688917488\"\n" + "         ], \n" + "         \"isAtAll\": false\n" + "     }\n" + " }");
        System.out.println(result);

    }

}
