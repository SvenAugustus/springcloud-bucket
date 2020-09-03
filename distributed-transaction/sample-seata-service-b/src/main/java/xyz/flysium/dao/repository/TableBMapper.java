package xyz.flysium.dao.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import xyz.flysium.dao.entity.TableB;
import xyz.flysium.dao.entity.TableBExample;

import java.util.List;

@Mapper
@Component
public interface TableBMapper {
    @Delete({
            "delete from tb_b",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
            "insert into tb_b (name)",
            "values (#{name,jdbcType=VARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(TableB record);

    @InsertProvider(type = TableBSqlProvider.class, method = "insertSelective")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    int insertSelective(TableB record);

    @SelectProvider(type = TableBSqlProvider.class, method = "selectByExample")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR)
    })
    List<TableB> selectByExampleWithRowbounds(TableBExample example, RowBounds rowBounds);

    @SelectProvider(type = TableBSqlProvider.class, method = "selectByExample")
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR)
    })
    List<TableB> selectByExample(TableBExample example);

    @Select({
            "select",
            "id, name",
            "from tb_b",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR)
    })
    TableB selectByPrimaryKey(Long id);

    @UpdateProvider(type = TableBSqlProvider.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TableB record);

    @Update({
            "update tb_b",
            "set name = #{name,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(TableB record);
}