package dev.tuvumba.theboringuniversity.service;


public interface CrudService<EntityDto, ID>{
    EntityDto save (EntityDto entity);
    Iterable<EntityDto> findAll();
    EntityDto findById(ID id);
    EntityDto update(ID id, EntityDto entity);
    void delete(ID id);
}

