$(function () {
    var static_prefix = '/static/assets/js/management';
    var url_prefix = '/management/';
    vipspa.start({
        view: '#admin-body',
        router: {
            'defaults': '/dashboard',//默认路由
            '/role': {baseUrl: url_prefix + 'role'},
            '/menu': {baseUrl: url_prefix + 'menu'},
            '/dashboard': {baseUrl: url_prefix + 'dashboard', controller: static_prefix + '/dashboard.js'},
            '/menu/add': {baseUrl: url_prefix + 'menu/add'},
            '/menu/edit': {baseUrl: url_prefix + 'menu/edit'},
            '/users': {baseUrl: url_prefix + 'users'},
            '/article': {baseUrl: url_prefix + 'article', controller: static_prefix + '/article_list.js'},
            '/article/post': {baseUrl: url_prefix + 'article/post', controller: static_prefix + '/article_post.js'},
            '/article/edit': {baseUrl: url_prefix + 'article/edit', controller: static_prefix + '/article_edit.js'},
            '/settings/common': {baseUrl: url_prefix + 'settings/common'},
            '/settings/mail': {baseUrl: url_prefix + 'settings/mail'},
            '/settings/profile': {baseUrl: url_prefix + 'settings/profile', controller: static_prefix + "/profile.js"},
            '/settings/qrcode': {baseUrl: url_prefix + 'settings/qrcode', controller: static_prefix + "/qrcode.js"},
            'note': {
                baseUrl: url_prefix + 'note',
                controller: static_prefix + '/note.js'
            },
            'notes': {
                baseUrl: url_prefix + 'note/index',
                controller: static_prefix + '/notes.js'
            },
            'note_edit': {
                baseUrl: url_prefix + 'note/edit',
                controller: static_prefix + '/note_edit.js'
            },
            'cate': {
                baseUrl: url_prefix + 'cate',
                controller: static_prefix + '/cate.js'
            },
            'file': {
                baseUrl: url_prefix + 'file',
                controller: static_prefix + '/file.js'
            },
            'tag': {
                baseUrl: url_prefix + 'tag',
                controller: static_prefix + '/tag.js'
            },
            'about': {
                baseUrl: url_prefix + 'about',
                controller: static_prefix + '/about.js'
            },
            'keyword': {
                baseUrl: url_prefix + 'keyword',
                controller: static_prefix + '/keyword.js'
            },
            'qrcode': {
                baseUrl: url_prefix + 'settings/qrcode',
                controller: static_prefix + '/qrcode.js'
            },
            'comment': {
                baseUrl: url_prefix + 'comment',
                controller: static_prefix + '/comment.js'
            },
            'message': {
                baseUrl: url_prefix + 'message',
                controller: static_prefix + '/msg.js'
            },
            'noteblog': {
                baseUrl: url_prefix + 'noteblog',
                controller: static_prefix + '/noteblog.js'
            }
        },
        errorTemplateId: '#error'
    });

});
