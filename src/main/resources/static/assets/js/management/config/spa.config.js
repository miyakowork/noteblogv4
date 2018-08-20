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
            '/dictionary/cate': {path: '/dictionary/cate', action: '/cate.js'},
            '/dictionary/tag': {path: '/dictionary/tag', action: '/tag.js'},
            '/dictionary/keyword': {path: '/dictionary/keyword', action: '/keyword.js'},
            '/note/post': {path: '/note/post', action: '/note_post.js'},
            '/note': {path: '/note', action: '/note_list.js'},
            '/note/edit': {path: '/note/edit', action: '/note_edit.js'},
            // 'file': {
            //     url: urlPrefix + 'file',
            //     controller: staticPrefix + '/file.js'
            // },
            // 'about': {
            //     url: urlPrefix + 'about',
            //     controller: staticPrefix + '/about.js'
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
