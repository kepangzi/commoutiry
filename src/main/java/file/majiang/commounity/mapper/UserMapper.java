package file.majiang.commounity.mapper;

import file.majiang.commounity.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user(name,account_id,token,create_time,gmt_modified) values(#{name},#{accountId},#{token},#{createTime},#{gmtModified})")
    void insert(User user);
    @Select("select * from user where token =#{token}")
    User findByToken(@Param("token") String token);
}
