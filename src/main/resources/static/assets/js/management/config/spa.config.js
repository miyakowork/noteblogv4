$(function () {
    var static_prefix = '/static/assets/js/management';
    var url_prefix = '/management/';
    vipspa.start({
        view: '#admin-body',
        router: {
            'defaults': '/dashboard',//默认路由
            '/role': {templateUrl: url_prefix + 'role'},
            '/menu': {templateUrl: url_prefix + 'menu'},
            '/dashboard': {templateUrl: url_prefix + 'dashboard', controller: static_prefix + '/dashboard.js'},
            '/menu/add': {templateUrl: url_prefix + 'menu/add'},
            '/menu/edit': {templateUrl: url_prefix + 'menu/edit'},
            '/users': {templateUrl: url_prefix + 'users'},
            '/article': {templateUrl: url_prefix + 'article', controller: static_prefix + '/article_list.js'},
            '/article/post': {templateUrl: url_prefix + 'article/post', controller: static_prefix + '/article_post.js'},
            '/article/edit': {templateUrl: url_prefix + 'article/edit', controller: static_prefix + '/article_edit.js'},
            '/settings': {templateUrl: url_prefix + 'settings'},
            'note': {
                templateUrl: url_prefix + 'note',
                controller: static_prefix + '/note.js'
            },
            'notes': {
                templateUrl: url_prefix + 'note/index',
                controller: static_prefix + '/notes.js'
            },
            'note_edit': {
                templateUrl: url_prefix + 'note/edit',
                controller: static_prefix + '/note_edit.js'
            },
            'cate': {
                templateUrl: url_prefix + 'cate',
                controller: static_prefix + '/cate.js'
            },
            'file': {
                templateUrl: url_prefix + 'file',
                controller: static_prefix + '/file.js'
            },
            'tag': {
                templateUrl: url_prefix + 'tag',
                controller: static_prefix + '/tag.js'
            },
            'about': {
                templateUrl: url_prefix + 'about',
                controller: static_prefix + '/about.js'
            },
            'keyword': {
                templateUrl: url_prefix + 'keyword',
                controller: static_prefix + '/keyword.js'
            },
            'qrcode': {
                templateUrl: url_prefix + 'settings/qrcode',
                controller: static_prefix + '/qrcode.js'
            },
            'comment': {
                templateUrl: url_prefix + 'comment',
                controller: static_prefix + '/comment.js'
            },
            'message': {
                templateUrl: url_prefix + 'message',
                controller: static_prefix + '/msg.js'
            },
            'noteblog': {
                templateUrl: url_prefix + 'noteblog',
                controller: static_prefix + '/noteblog.js'
            },
            'profile': {
                templateUrl: url_prefix + 'profile',
                controller: static_prefix + '/profile.js'
            }
        },
        errorTemplateId: '#error'
    });

});
