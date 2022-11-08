package kitchenpos.table.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.table.domain.OrderTable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderTableDao implements OrderTableDao {

    private static final String TABLE_NAME = "order_table";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderTableDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        if (Objects.isNull(entity.getId())) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            return select(key.longValue());
        }
        update(entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderTable> findAll() {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty, ordered FROM order_table";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty, ordered FROM order_table WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty, ordered" +
                " FROM order_table WHERE table_group_id = (:tableGroupId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public void updateAll(final List<OrderTable> entities) {
        final String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
                " number_of_guests = (:numberOfGuests), empty = (:empty), ordered = (:ordered) WHERE id = (:id)";

        final List<MapSqlParameterSource> params = new ArrayList<>();
        for (final OrderTable entity : entities) {
            final MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("tableGroupId", entity.getTableGroupId())
                    .addValue("numberOfGuests", entity.getNumberOfGuests())
                    .addValue("empty", entity.isEmpty())
                    .addValue("ordered", entity.isOrdered())
                    .addValue("id", entity.getId());
            params.add(param);
        }
        jdbcTemplate.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
    }

    private OrderTable select(final Long id) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty, ordered FROM order_table WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private void update(final OrderTable entity) {
        final String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
                " number_of_guests = (:numberOfGuests), empty = (:empty), ordered = (:ordered) WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", entity.getTableGroupId())
                .addValue("numberOfGuests", entity.getNumberOfGuests())
                .addValue("empty", entity.isEmpty())
                .addValue("ordered", entity.isOrdered())
                .addValue("id", entity.getId());
        jdbcTemplate.update(sql, parameters);
    }

    private OrderTable toEntity(final ResultSet resultSet) throws SQLException {
        return new OrderTable(
                resultSet.getLong(KEY_COLUMN_NAME),
                resultSet.getObject("table_group_id", Long.class),
                resultSet.getInt("number_of_guests"),
                resultSet.getBoolean("empty"),
                resultSet.getBoolean("ordered")
        );
    }
}