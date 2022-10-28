package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

public interface MenuDao {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}

@Repository
class MenuRepository implements MenuDao {
    private final JdbcTemplateMenuDao menuDao;
    private final JdbcTemplateMenuProductDao menuProductDao;

    public MenuRepository(final JdbcTemplateMenuDao menuDao, final JdbcTemplateMenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final Long menuId = entity.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        entity.setMenuProducts(savedMenuProducts);

        return menuDao.save(entity);
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
