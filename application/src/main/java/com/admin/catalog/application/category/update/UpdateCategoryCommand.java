package com.admin.catalog.application.category.update;

public record UpdateCategoryCommand(
        String id,
        String name,
        String description,
        boolean isActive
) {

    public static UpdateCategoryCommand with(
            String anId,
            String aName,
            String aDescription,
            boolean isActive
    ){
        return new UpdateCategoryCommand(anId,aName,aDescription,isActive);
    }
}
