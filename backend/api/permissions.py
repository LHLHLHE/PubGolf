from rest_framework import permissions


class CompanyPermission(permissions.BasePermission):

    def has_permission(self, request, view):
        return (request.user.is_authenticated
                and request.user.is_company)

    def has_object_permission(self, request, view, obj):
        return (request.user.is_authenticated
                and request.user.is_company)


class ReadOnlyPermission(permissions.BasePermission):

    def has_permission(self, request, view):
        return request.method in permissions.SAFE_METHODS

    def has_object_permission(self, request, view, obj):
        return request.method in permissions.SAFE_METHODS
