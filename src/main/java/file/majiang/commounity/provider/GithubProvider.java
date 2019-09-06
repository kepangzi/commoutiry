package file.majiang.commounity.provider;

import com.alibaba.fastjson.JSON;
import file.majiang.commounity.dto.AccessTokenDTO;
import file.majiang.commounity.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class GithubProvider {
    //使用okHttp POST方式访问
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String[] spilt = string.split("&");
            String[] tokenStr = spilt[0].split("=");
            String token = tokenStr[1];
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        //访问github返回user数据
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            //把json数据转化成对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
