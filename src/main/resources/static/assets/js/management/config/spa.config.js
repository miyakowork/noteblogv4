$(function () {
    vipspa.start({
        view: '#admin-body',
        basePath: '/management',
        baseStatic: '/static/assets/js/management',
        router: {
            'defaults': '/dashboard',//默认路由
            '/role': {path: '/role'},
            '/menu': {path: '/menu'},
            '/dashboard': {path: '/dashboard', action: '/dashboard.js'},
            '/menu/add': {path: '/menu/add'},
            '/menu/edit': {path: '/menu/edit'},
            '/users': {path: '/users'},
            '/article': {path: '/article', action: '/article_list.js'},
            '/article/post': {path: '/article/post', action: '/article_post.js'},
            '/article/edit': {path: '/article/edit', action: '/article_edit.js'},
            '/settings/common': {path: '/settings/common'},
            '/settings/mail': {path: '/settings/mail'},
            '/settings/profile': {path: '/settings/profile', action: "/profile.js"},
            '/settings/qrcode': {path: '/settings/qrcode', action: "/qrcode.js"},
            '/cate': {path: '/dictionary/cate', action: '/cate.js'},
            // 'note': {
            //     url: urlPrefix + 'note',
            //     controller: staticPrefix + '/note.js'
            // },
            // 'notes': {
            //     url: urlPrefix + 'note/index',
            //     controller: staticPrefix + '/notes.js'
            // },
            // 'note_edit': {
            //     url: urlPrefix + 'note/edit',
            //     controller: staticPrefix + '/note_edit.js'
            // },
            // 'file': {
            //     url: urlPrefix + 'file',
            //     controller: staticPrefix + '/file.js'
            // },
            // 'tag': {
            //     url: urlPrefix + 'tag',
            //     controller: staticPrefix + '/tag.js'
            // },
            // 'about': {
            //     url: urlPrefix + 'about',
            //     controller: staticPrefix + '/about.js'
            // },
            // 'keyword': {
            //     url: urlPrefix + 'keyword',
            //     controller: staticPrefix + '/keyword.js'
            // },
            // 'qrcode': {
            //     url: urlPrefix + 'settings/qrcode',
            //     controller: staticPrefix + '/qrcode.js'
            // },
            // 'comment': {
            //     url: urlPrefix + 'comment',
            //     controller: staticPrefix + '/comment.js'
            // },
            // 'message': {
            //     url: urlPrefix + 'message',
            //     controller: staticPrefix + '/msg.js'
            // },
            // 'noteblog': {
            //     url: urlPrefix + 'noteblog',
            //     controller: staticPrefix + '/noteblog.js'
            // }
        },
        errorTemplateId: '#error'
    });

});
