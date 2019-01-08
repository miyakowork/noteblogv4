var template = {
    footer:
        '<div class="layui-fluid bottom-nav">' +
        '   <div class="layui-row">' +
        '       <div class="layui-col-md4 layui-col-md-offset4 copyright">' +
        '           <p style="text-align: center;">' +
        '               <i class="fa fa-copyright"></i>&nbsp;{{words}} powered by' +
        '                <a href="//wuwenbin.me" target="_blank" style="color: #1E9FFF;">NoteBlog</a> in 2018 ver4.' +
        '           </p>' +
        '       </div>' +
        '   </div> ' +
        '</div>'
    ,
    header:
        '<div class="header">' +
        '    <div class="layui-container">' +
        '        <div class="layui-row nav-header">' +
        '            <div class="layui-col-xs9 layui-col-sm4">' +
        '                <a class="logo" href="/index"><i class="fa fa-graduation-cap"></i>&nbsp;{{params.website_logo_words}}</a>' +
        '                <h2 id="title" style="display: none;margin-left: 10%;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" v-if="st">{{title}}</h2>' +
        '            </div>' +
        '            <div class="layui-col-xs3 layui-col-sm-offset2 layui-col-sm4 layui-hide-md layui-hide-lg nav-btn">' +
        '                <a href="javascript:void(0);" id="side-nav"><i class="fa fa-navicon"></i> </a>' +
        '            </div>' +
        '            <div class="layui-col-sm7 layui-hide layui-show-lg-inline-block layui-show-md-inline-block nav-btn">' +
        '                <a href="/index" :class="{ active: home }"><i class="layui-icon">&#xe68e;</i> {{params.menu_home}}</a>' +
        '                <a href="/note" :class="{ active: note }"><i class="layui-icon">&#xe609;</i> {{params.menu_note}}</a>' +
        '                <a href="/profile" :class="{ active: mine }"><i class="layui-icon">&#xe6af;</i> {{params.menu_mine}}</a>' +
        '                <a href="javascript:;" :class="{ active: search }" target="_blank"><i class="layui-icon">&#xe615;</i> {{params.menu_search}}</a>' +
        '                <a v-show="params.menu_link_show == 1" :href="params.menu_link_href" target="_blank"><i :class="params.menu_link_icon" style="font-size: 16px;"></i> {{params.menu_link}}</a>' +
        '            </div>' +
        '            <ul class="layui-nav layui-nav-tree layui-nav-side" id="mobile-nav">' +
        '                <li class="layui-nav-item">' +
        '                   <a href="/index" :class="{ \'layui-this\': home }"><i class="layui-icon">&#xe68e;</i> {{params.menu_home}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item">' +
        '                   <a href="/note" :class="{ \'layui-this\': note }"><i class="layui-icon">&#xe609;</i> {{params.menu_note}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item">' +
        '                   <a href="/profile" :class="{ \'layui-this\': mine }"><i class="layui-icon">&#xe715;</i> {{params.menu_mine}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item">' +
        '                   <a href="javascript:;" :class="{ \'layui-this\': search }"><i class="layui-icon">&#xe615;</i> {{params.menu_search}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item" v-if="params.menu_link_show == 1">' +
        '                     <a :href="params.menu_link_href" target="_blank"><i :class="params.menu_link_icon" style="font-size: 15px;"></i> {{params.menu_link}}</a>' +
        '                </li>' +
        '            </ul>' +
        '        </div>' +
        '    </div>' +
        '</div>'
    ,
    headerNoTxt:
        '<div class="header mini">' +
        '    <div class="layui-container">' +
        '        <div class="layui-row nav-header">' +
        '            <div class="layui-col-xs9 layui-col-sm4">' +
        '                <a class="logo" href="/index"><i class="fa fa-graduation-cap"></i>&nbsp;{{params.website_logo_words}}</a><small v-show="showSmall">{{params.website_logo_small_words}}</small>' +
        '                <h2 id="title" style="display: none;margin-left: 10%;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" v-if="st">{{title}}</h2>' +
        '            </div>' +
        '            <div class="layui-col-xs3 layui-col-sm-offset2 layui-col-sm4 layui-hide-md layui-hide-lg nav-btn">' +
        '                <a href="javascript:void(0);" id="side-nav-open" onclick="hideOpen(this)" data-pushbar-target="bottomNav"><i class="fa fa-navicon"></i> </a>' +
        '                <a href="javascript:void(0);" id="side-nav-close" onclick="hideClose(this)" style="display: none;" data-pushbar-close><i class="fa fa-times" style="font-size: 18px;"></i> </a>' +
        '            </div>' +
        '            <div class="layui-col-sm5 layui-hide layui-show-lg-inline-block layui-show-md-inline-block nav-btn right">' +
        '                <a id="ass" href="/index" :class="{ active: home }" :data-title="params.menu_home">' +
        '                       <i class="layui-icon layui-icon-home"></i> ' +
        '                       <span>{{params.menu_home}}</span>' +
        '                   </a>' +
        '                <a v-show="params.menu_note_show == 1" href="/note" :class="{ active: note }" :data-title="params.menu_note">' +
        '                       <i class="layui-icon layui-icon-form" style="font-size: 19px !important;"></i> ' +
        '                       <span>{{params.menu_note}}</span>' +
        '               </a>' +
        '                <a v-show="params.menu_project_show == 1" href="/project" :class="{ active: project }" :data-title="params.menu_project">' +
        '                       <i class="layui-icon layui-icon-app"></i> ' +
        '                       <span>{{params.menu_project}}</span>' +
        '                   </a>' +
        '                <a v-show="params.menu_mine_show == 1" href="/profile" :class="{ active: mine }" :data-title="params.menu_mine">' +
        '                       <i class="layui-icon layui-icon-user"></i> ' +
        '                       <span>{{params.menu_mine}}</span>' +
        '               </a>' +
        '                <a v-show="params.menu_search_show == 1" style="cursor:pointer;" @click="searchDialog" :class="{ active: search }" :data-title="params.menu_search" target="_blank">' +
        '                       <i class="layui-icon layui-icon-search"></i> ' +
        '                       <span>{{params.menu_search}}</span>' +
        '               </a>' +
        '                <a v-show="params.menu_link_show == 1" :href="params.menu_link_href" target="_blank" :data-title="params.menu_link">' +
        '                       <i :class="params.menu_link_icon" style="font-size: 16px;"></i> ' +
        '                       <span>{{params.menu_link}}</span>' +
        '               </a>' +
        '            </div>' +
        '            <ul class="layui-nav layui-nav-tree layui-nav-side" id="mobile-nav">' +
        '                <li class="layui-nav-item">' +
        '                   <a href="/index" :class="{ \'layui-this\': home }"><i class="layui-icon">&#xe68e;</i> {{params.menu_home}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item">' +
        '                   <a href="/note" :class="{ \'layui-this\': note }"><i class="layui-icon">&#xe609;</i> {{params.menu_note}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item">' +
        '                   <a href="/profile" :class="{ \'layui-this\': mine }"><i class="layui-icon">&#xe715;</i> {{params.menu_mine}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item">' +
        '                   <a href="javascript:;" :class="{ \'layui-this\': search }"><i class="layui-icon">&#xe615;</i> {{params.menu_search}}</a>' +
        '                </li>' +
        '                <li class="layui-nav-item" v-if="params.menu_link_show == 1">' +
        '                     <a :href="params.menu_link_href" target="_blank"><i :class="params.menu_link_icon" style="font-size: 15px;"></i> {{params.menu_link}}</a>' +
        '                </li>' +
        '            </ul>' +
        '        </div>' +
        '    </div>' +
        '</div>'
    ,
    block:
        '<div class="layui-container" style="margin-top: 90px;">' +
        '        <blockquote class="layui-elem-quote" style="border-left: 5px solid rgba(244, 67, 54, 1);">' +
        '            <template v-if="quote.showBlog">博文统计：共【<span class="sum-font">{{quote.blogCount}}</span>】篇；</template>' +
        '            <template v-if="quote.showNote">笔记统计：共【<span class="sum-font">{{quote.noteCount}}</span>】条；</template>' +
        '            <template v-if="quote.showFile">资源统计：共【<span class="sum-font">{{quote.fileCount}}</span>】条；</template>' +
        '            <template v-if="quote.showSearch">搜索统计：共【<span class="sum-font">{{quote.searchCount}}</span>】个；</template>' +
        '            <span v-if="quote.showText" v-html="quote.text"></span>' +
        '            <div v-show="quote.showClock" class="clock layui-show-md-inline-block layui-show-lg-inline-block layui-hide-xs layui-hide-sm">' +
        '                <span class="clock-font">当前日期</span>：<span id="current-datetime"></span>' +
        '            </div>' +
        '        </blockquote>' +
        '    </div>'
    ,
    articles:
        '<div id="main-body" class="layui-container">' +
        '        <div class="layui-row layui-col-space10">' +
        '            <slot name="list"></slot>' +
        '            <div class="layui-col-md3">' +
        '                <div id="affix-side">' +
        '                   <slot name="infoLabel"></slot>' +
        '                   <slot name="search"></slot>' +
        '                   <slot name="cate"></slot>' +
        '                   <slot name="random"></slot>' +
        '                   <slot name="tab"></slot>' +
        '                </div>' +
        '            </div>' +
        '        </div>' +
        '    </div>'

    ,
    article:
        '<div id="article-list" class="layui-col-md9"></div>'
    ,
    info:
        '<div class="layui-tab layui-tab-card">' +
        '   <ul class="layui-tab-title select-none">' +
        '       <template v-if="order == 1">' +
        '       <li class="layui-this" v-show="info">网站信息</li>' +
        '       <li :class="{ \'layui-this\' : !info }">会员中心</li>' +
        '       </template>' +
        '       <template v-if="order != 1">' +
        '           <li class="layui-this">会员中心</li>' +
        '           <li :class="{ \'layui-this\' : !info }" v-show="info">网站信息</li>' +
        '       </template>' +
        '   </ul>' +
        '   <div class="layui-tab-content">' +
        '       <template v-if="order == 1">' +
        '       <div class="layui-tab-item layui-show layui-text" v-html="utext" v-show="info">' +
        '           {{ utext }}' +
        '       </div>' +
        '       <div class="layui-tab-item" :class="{ \'layui-show\' : !info }">' +
        '           <p style="text-align: center" v-show="su == null">' +
        '               <a v-if="qq == 1" @click="tip()" href="/api/qq" class="layui-btn layui-btn-sm layui-btn-primary">' +
        '                   <i class="fa fa-qq"></i> 网站用户' +
        '               </a>' +
        '               <a href="/login" class="layui-btn layui-btn-sm layui-btn-primary"><i class="fa fa-user-o"></i> 网站管理</a>' +
        '           </p>' +
        '           <p v-if="su != null" style="padding-left: 20px;">' +
        '               <a href="/token/logout" style="font-size: 14px;">' +
        '                   <img id="logout" @mouseover="tipsOver()" @mouseout="tipsOut()" style="height: 45px;width: 45px;border: 1px solid #ccc;" class="layui-circle" :src="su != null ?su.avatar:\'\'">&nbsp;&nbsp;欢迎你，{{su.nickname}}！' +
        '               </a>' +
        '               &nbsp;<a v-if="su !=null && su.dri == 1" href="/management/index" target="_blank" class="layui-text" style="font-style: italic;color: #F44336;">后台管理 </a>' +
        '           </p>' +
        '       </div>' +
        '       </template>' +
        '       <template v-if="order != 1">' +
        '       <div class="layui-tab-item layui-show">' +
        '           <p style="text-align: center" v-show="su == null">' +
        '               <a v-if="qq == 1" @click="tip()" href="/api/qq" class="layui-btn layui-btn-sm layui-btn-primary">' +
        '                   <i class="fa fa-qq"></i> 网站用户' +
        '               </a>' +
        '               <a href="/login" class="layui-btn layui-btn-sm layui-btn-primary"><i class="fa fa-user-o"></i> 网站管理</a>' +
        '           </p>' +
        '           <p v-if="su != null" style="padding-left: 20px;">' +
        '               <a href="/token/logout" style="font-size: 14px;">' +
        '                   <img id="logout" @mouseover="tipsOver()" @mouseout="tipsOut()" style="height: 45px;width: 45px;" class="layui-circle" :src="su != null ?su.avatar:\'\'">&nbsp;&nbsp;欢迎你，{{su.nickname}}！' +
        '               </a>' +
        '               &nbsp;<a v-if="su !=null && su.dri == 1" href="/management/index" target="_blank" class="layui-text" style="font-style: italic;color: #F44336;">后台管理 </a>' +
        '           </p>' +
        '       </div>' +
        '       <div class="layui-tab-item layui-text" v-html="utext" v-show="info">' +
        '           {{ utext }}' +
        '       </div>' +
        '       </template>' +
        '   </div>' +
        '</div>'
    ,
    search:
        '<div id="search-panel" class="layui-tab layui-tab-card">' +
        '   <div class="layui-tab-content select-none">' +
        '       <p class="title">搜索库 <small style="float: right;"><a style="cursor: pointer;" @click="aboutSearch" target="_blank"><i>关于 <i class="fa fa-info-circle"></i></i></a></small> </p>' +
        '       <hr>' +
        '       <input name="words" v-model="words" @keyup.enter="searchAll" :value="sw" @input="updateValue($event.target.value)" ' +
        '           placeholder="键入Enter键以搜索" autocomplete="off" class="layui-input search-box">' +
        '   </div>' +
        '</div>'
    ,
    cate:
        '<div class="layui-tab layui-tab-card layui-article-cate">' +
        '   <div class="layui-tab-content select-none">' +
        '       <p class="title">分类堆</p>' +
        '       <hr>' +
        '       <p><a href="/index?c="><i class="layui-icon">&#xe60a;</i> 全部</a> </p>' +
        '       <template v-for="c in cates">' +
        '           <p><a :href="cateUrl(c)"><i class="layui-icon">&#xe60a;</i> {{c.cnName}}</a></p>' +
        '       </template>' +
        '   </div> ' +
        '</div>'
    ,
    random:
        '<div class="layui-tab layui-tab-card layui-master-recommend">' +
        '   <div class="layui-tab-content select-none">' +
        '       <p class="title">博文栈</p>' +
        '       <hr>' +
        '       <template v-for="r in articles">' +
        '           <p><a :href="\'/article/\'+r.id">{{subTitle(r.title)}}</a> </p>' +
        '       </template>' +
        '   </div> ' +
        '</div>'
    ,
    tab:
        '<div class="layui-tab layui-tab-card layui-tags">' +
        '   <div class="layui-tab-content select-none">' +
        '       <p class="title">标签页</p>' +
        '       <hr>' +
        '       <template v-for="t in tabs">' +
        '           <span class="layui-badge-rim" @click="find(t.name)">{{t.name}} ({{t.cnt}})</span>' +
        '       </template>' +
        '   </div> ' +
        '</div>'
    ,
    articlePage:
        '<div id="blog-body" class="layui-container">' +
        '   <div class="layui-row layui-col-space10">' +
        '       <div id="blog-info" class="layui-col-md9">' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <div class="layui-colla-item">' +
        '                   <div class="layui-colla-content layui-show layui-article">' +
        '                       <fieldset class="layui-elem-field layui-field-title layui-article-page-title">' +
        '                           <legend class="center-to-head" align="center">{{article.title}}</legend>' +
        '                       </fieldset>' +
        '                       <div class="layui-text layui-blog-body">' +
        '                           <div class="layui-row">' +
        '                               <div class="layui-col-md6 layui-col-md-offset3 text-center blog-base-info">' +
        '                                   <span><i class="fa fa-clock-o"></i> {{postDate}}</span>' +
        '                                   <span><i class="fa fa-user-o"></i> <span style="color: #FF5722;">{{author}}</span><svg class="icon" aria-hidden="true"><use xlink:href="#icon-renzhengkaobei"></use></svg></span>' +
        '                                   <span><i class="fa fa-comment-o"></i> {{comments}}</span>' +
        '                                   <span><i class="fa fa-eye"></i> {{article.view}}</span>' +
        '                               </div>' +
        '                           </div>' +
        '                           <hr>' +
        '                           <div class="content detail" v-html="article.content"></div>' +
        '                       </div>' +
        '                       <div class="layui-row text-center layui-mt20">' +
        '                           <div v-if="article.appreciable" class="layui-btn layui-btn-warm layui-hide layui-show-md-inline-block" @click="money(alipay,wechat)"><i class="fa fa-rmb"></i> 打赏</div>' +
        '                           <div class="layui-btn layui-btn-danger" @click="emotion()"><i class="fa fa-thumbs-o-up"></i> 赞 ({{approve}})</div>' +
        '                       </div>' +
        '                       <div class="layui-row layui-mt20">' +
        '                           <blockquote class="layui-elem-quote text-center " style="border: none;">' +
        '                               <span class="layui-show-md-inline-block layui-hide">文章出处：{{name}}</span>&nbsp;&nbsp;&nbsp;&nbsp;' +
        '                               <span class="layui-show-md-inline-block layui-hide">文章地址：{{url}}</span>&nbsp;&nbsp;&nbsp;&nbsp;' +
        '                               <span>转载注明下哦！o(≧v≦)o~~</span>' +
        '                           </blockquote>' +
        '                       </div>' +
        '                       <div class="layui-row layui-mt20">' +
        '                           <p class="blog-tags">' +
        '                           标签：' +
        '                               <template v-for="t in tags">' +
        '                                   <span>{{t.name}}</span>' +
        '                               </template>' +
        '                           </p>' +
        '                       </div>' +
        '                       <div class="layui-row layui-col-space20 layui-mt20 article-page-similar">' +
        '                           <p>相似文章：</p>' +
        '                           <hr>' +
        '                           <ul>' +
        '                               <template v-for="(a,index) in similars">' +
        '                                   <li><a :href="\'/article/\'+a.id" class="layui-word-aux"><i class="fa fa-circle-thin"></i>&nbsp;&nbsp;{{a.title}}</a> </li>' +
        '                               </template>' +
        '                           </ul>' +
        '                       </div>' +
        '                   </div>' +
        '               </div>' +
        '           </div>' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <slot name="post"></slot>' +
        '           </div>' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <slot name="comment"></slot>' +
        '           </div>' +
        '       </div>' +
        '       <div class="layui-col-md3">' +
        '           <div id="affix-side">' +
        '               <slot name="info"></slot>' +
        '               <slot name="search"></slot>' +
        '               <slot name="cate"></slot>' +
        '               <slot name="similar"></slot>' +
        '           </div>' +
        '       </div>' +
        '   </div>' +
        '</div>'

    ,
    articlePageMini:
        '<div id="blog-body" class="layui-container simple animated fadeInUp" style="margin-top: 100px;">' +
        '   <div class="layui-row layui-col-space10">' +
        '       <div id="blog-info" class="layui-col-md12">' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <div class="layui-colla-item">' +
        '                   <div class="layui-colla-content layui-show layui-article">' +
        '                       <fieldset class="layui-elem-field layui-field-title layui-article-page-title">' +
        '                           <legend class="center-to-head" align="center">{{article.title}}</legend>' +
        '                       </fieldset>' +
        '                       <div class="layui-text layui-blog-body">' +
        '                           <div class="layui-row">' +
        '                               <div class="layui-col-md6 layui-col-md-offset3 text-center blog-base-info">' +
        '                                   <span><i class="fa fa-clock-o"></i> {{postDate}}</span>' +
        '                                   <span><i class="fa fa-user-o"></i> <span style="color: #FF5722;">{{author}}</span><svg class="icon" aria-hidden="true"><use xlink:href="#icon-renzhengkaobei"></use></svg></span>' +
        '                                   <span><i class="fa fa-comment-o"></i> {{comments}}</span>' +
        '                                   <span><i class="fa fa-eye"></i> {{article.view}}</span>' +
        '                               </div>' +
        '                           </div>' +
        '                           <hr>' +
        '                           <div id="doc-content" class="content detail layui-col-sm12" v-if="!isRichTxt" style="margin-bottom: 20px;"></div>' +
        '                           <div id="doc-content1" class="content detail layui-col-sm12" v-if="isRichTxt" v-html="article.content" style="margin-bottom: 20px;"></div>' +
        '                           <div id="custom-toc-container" style="margin-left: 15px;display: none;"></div>' +
        '                       </div>' +
        '                       <div class="layui-row text-center layui-mt20">' +
        '                           <div v-if="article.appreciable" class="layui-btn layui-btn-warm layui-hide layui-show-md-inline-block" @click="money(alipay,wechat)"><i class="fa fa-rmb"></i> 打赏</div>' +
        '                           <div class="layui-btn layui-btn-danger" @click="emotion()"><i class="fa fa-thumbs-o-up"></i> 赞 ({{approve}})</div>' +
        '                       </div>' +
        '                       <div class="layui-row layui-mt20">' +
        '                           <blockquote class="layui-elem-quote text-center " style="border: none;">' +
        '                               <span class="layui-show-md-inline-block layui-hide">文章出处：{{name}}</span>&nbsp;&nbsp;&nbsp;&nbsp;' +
        '                               <span class="layui-show-md-inline-block layui-hide">文章地址：{{url}}</span>&nbsp;&nbsp;&nbsp;&nbsp;' +
        '                               <span>转载注明下哦！o(≧v≦)o~~</span>' +
        '                           </blockquote>' +
        '                       </div>' +
        '                       <div class="layui-row layui-mt20">' +
        '                           <p class="blog-tags">' +
        '                           标签：' +
        '                               <template v-for="t in tags">' +
        '                                   <span>{{t.name}}</span>' +
        '                               </template>' +
        '                           </p>' +
        '                       </div>' +
        '                       <div class="layui-row layui-col-space20 layui-mt20 article-page-similar">' +
        '                           <p>相似文章：</p>' +
        '                           <hr>' +
        '                           <ul>' +
        '                               <template v-for="(a,index) in similars">' +
        '                                   <li><a :href="\'/article/\'+a.id" class="layui-word-aux"><i class="fa fa-circle-thin"></i>&nbsp;&nbsp;{{a.title}}</a> </li>' +
        '                               </template>' +
        '                           </ul>' +
        '                       </div>' +
        '                   </div>' +
        '               </div>' +
        '           </div>' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <slot name="post"></slot>' +
        '           </div>' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <slot name="comment"></slot>' +
        '           </div>' +
        '       </div>' +
        '   </div>' +
        '</div>'

    ,
    similar:
        '<div class="layui-tab layui-tab-card layui-similar">' +
        '   <div class="layui-tab-content select-none">' +
        '       <p class="title">相似文章</p>' +
        '       <hr>' +
        '       <template v-for="a in articles">' +
        '           <p><a :href="\'/article/\'+a.id">{{subTitle(a.title)}}</a> </p>' +
        '       </template>' +
        '   </div> ' +
        '</div>'
    ,
    comment:
        '<div id="cta" class="layui-collapse layui-panel layui-article">' +
        '   <div class="layui-colla-item">' +
        '       <div class="layui-colla-content layui-show layui-article comment">' +
        '           <fieldset class="layui-elem-field layui-field-title">' +
        '               <legend>随便说两句</legend>' +
        '               <div class="layui-field-box">' +
        '                   <label for="comment-input"></label>' +
        '                       <textarea id="comment-input" style="display: none;"></textarea>' +
        '               </div>' +
        '               <button v-if="su != null" class="layui-btn-danger layui-btn layui-btn-sm" style="float: right;width: 120px;" @click="submit(id)" >发表</button>' +
        '               <a v-if="su == null" class="layui-btn layui-btn-sm" style="float: right;width: 120px;background-color: #f44336;"  href="/login" target="_blank" @click="beforeLogin();" id="beforeLogin"><i class="fa fa-qq"></i> 请先登录</a>' +
        '           </fieldset>' +
        '       </div>' +
        '   </div>' +
        '</div>'
    ,
    messageComment:
        '<div id="cta" class="layui-collapse layui-panel layui-article">' +
        '   <div class="layui-colla-item">' +
        '       <div class="layui-colla-content layui-show layui-article comment">' +
        '           <fieldset class="layui-elem-field layui-field-title">' +
        '               <legend>随便说两句</legend>' +
        '               <div class="layui-field-box">' +
        '                   <label for="comment-input"></label>' +
        '                       <textarea id="comment-input" style="display: none;"></textarea>' +
        '               </div>' +
        '               <button v-if="su != null" class="layui-btn layui-btn-sm layui-btn-danger" style="float: right;width: 120px;" @click="submit()" >发表</button>' +
        '               <a v-if="su == null" class="layui-btn layui-btn-sm" style="float: right;width: 120px;background-color: #f44336;"  href="/api/qq" target="_blank" @click="beforeLogin();" id="beforeLogin"><i class="fa fa-qq"></i> 请先登录</a>' +
        '           </fieldset>' +
        '       </div>' +
        '   </div>' +
        '</div>'
    ,
    commentArea:
        '<div class="layui-collapse layui-panel layui-article" id="ca">' +
        '   <div class="layui-colla-item">' +
        '       <div class="layui-colla-content layui-show layui-article comment">' +
        '           <fieldset class="layui-elem-field layui-field-title">' +
        '               <legend>网友评论・留言区</legend>' +
        '               <div class="layui-field-box comment">' +
        '                   <p v-if="comments.totalCount == 0" class="text-center">暂无评论</p>' +
        '                   <blockquote class="layui-elem-quote layui-mt20" style="border-left: 5px solid #F44336;color: #FF5722;" v-html="tips"></blockquote>' +
        '                   <template v-for="c in comments.content">' +
        '                       <div class="layui-row comment-block" v-if="c.enable">' +
        '                           <div class="layui-row">' +
        '                               <div class="layui-col-md1 layui-col-xs1 comment-avatar">' +
        '                                   <img class="layui-circle" :src="c.user.avatar">' +
        '                               </div>' +
        '                               <div class="layui-col-md10 layui-col-xs9" style="border-bottom: 1px dotted #dbdbdb;padding-bottom: 10px;">' +
        '                                   <i class="fa fa-user-o"></i> <span class="comment-user" :style="masterColor(c.userId)">{{c.nickname}}&nbsp;<svg v-if="c.userId==1" class="icon" aria-hidden="true"><use xlink:href="#icon-renzhengkaobei"></use></svg>&nbsp;&nbsp;</span><small><i class="fa fa-location-arrow"></i> {{c.ipCnAddr}}网友</small><br/>' +
        '                                   <i class="fa fa-clock-o"></i> <span class="comment-datetime">{{postDt(c.post)}}</span>' +
        '                               </div>' +
        '                               <div class="layui-row comment-block-content">' +
        '                                   <p  v-html="c.comment"></p>' +
        '                               </div>' +
        '                               <div class="layui-row" style="text-align: right;" v-show="re">' +
        '                                    <a @click="reback(c.user.nickname,c.comment);" style="cursor: pointer;"> <i class="fa fa-comment"></i> 回复</a>' +
        '                               </div>' +
        '                           </div>' +
        '                       </div>' +
        '                       <hr>' +
        '                   </template>' +
        '                   <div class="row">' +
        '                       <p class="comment-page"></p>' +
        '                   </div>' +
        '               </div>' +
        '           </fieldset>' +
        '       </div>' +
        '   </div>' +
        '</div>'

    ,
    simpleCommentArea:
        '<div class="layui-collapse layui-panel layui-article" id="ca">' +
        '   <div class="layui-colla-item">' +
        '       <div class="layui-colla-content layui-show layui-article comment">' +
        '           <fieldset class="layui-elem-field layui-field-title">' +
        '               <legend>网友评论・留言区</legend>' +
        '               <div class="layui-field-box comment">' +
        '                   <p v-if="comments.totalCount == 0" class="text-center">暂无评论</p>' +
        '                   <blockquote class="layui-elem-quote layui-mt20" style="border-left: 5px solid #F44336;" v-html="tips"></blockquote>' +
        '                   <template v-for="c in comments.content">' +
        '                       <div class="layui-row comment-block" v-if="c.enable">' +
        '                           <div class="layui-row">' +
        '                               <div class="layui-col-md1 layui-col-xs1 comment-avatar">' +
        '                                   <img class="layui-circle" :src="c.user.avatar">' +
        '                               </div>' +
        '                               <div class="layui-col-md10 layui-col-xs9" style="border-bottom: 1px dotted #dbdbdb;padding-bottom: 10px;">' +
        '                                   <i class="fa fa-user-o"></i> <span class="comment-user" :style="masterColor(c.userId)">{{c.nickname}}&nbsp;<svg v-if="c.userId==1" class="icon" aria-hidden="true"><use xlink:href="#icon-renzhengkaobei"></use></svg>&nbsp;&nbsp;</span><small><i class="fa fa-location-arrow"></i> {{c.ipCnAddr}}网友</small><br/>' +
        '                                   <i class="fa fa-clock-o"></i> <span class="comment-datetime">{{postDt(c.post)}}</span>' +
        '                               </div>' +
        '                               <div class="layui-row comment-block-content">' +
        '                                   <p  v-html="c.comment"></p>' +
        '                               </div>' +
        '                               <div class="layui-row" style="text-align: right;" v-show="re">' +
        '                                    <a @click="reback(c.user.nickname,c.comment);" style="cursor: pointer;"> <i class="fa fa-comment"></i> 回复</a>' +
        '                               </div>' +
        '                           </div>' +
        '                       </div>' +
        '                       <hr>' +
        '                   </template>' +
        '                   <div class="row">' +
        '                       <p class="comment-page"></p>' +
        '                   </div>' +
        '               </div>' +
        '           </fieldset>' +
        '       </div>' +
        '   </div>' +
        '</div>'
    ,
    notePage:
        '<div id="note-body" class="layui-container">' +
        '   <div id="note-operate" class="layui-row">' +
        '       <div class="layui-col-lg4 layui-col-md4 layui-col-sm5 layui-col-xs12 animated fadeInUp">' +
        '           <input name="words" v-model="words" @keyup.enter="searchAll"  :value="sw" @input="updateValue($event.target.value)" ' +
        '                       placeholder="输入关键字，按【Enter/回车】键搜索" autocomplete="off" class="layui-input search-box" style="height: 35px; ">' +
        '       </div>' +
        '       <div class="layui-col-lg-offset4 layui-col-lg4 layui-col-md-offset4 layui-col-md4 layui-col-sm-offset3 layui-col-sm4 layui-hide-xs" style="text-align: right;">' +
        '           <div class="layui-btn-group">' +
        '               <button id="collaspan" class="layui-btn layui-btn-primary layui-btn-sm">' +
        '                    <i class="fa fa-angle-double-down"></i>' +
        '                </button>' +
        '               <button id="expand" class="layui-btn layui-btn-primary layui-btn-sm">' +
        '                    <i class="fa fa-angle-double-up"></i>' +
        '                </button>' +
        '           </div>' +
        '       </div>' +
        '   </div>' +
        '   <hr class="animated fadeInUp">' +
        '   <ul class="layui-timeline" id="timeline">' +
        '       <li class="layui-timeline-item layui-note-cover">' +
        '            <i class="layui-icon layui-timeline-axis">&#xe63f;</i>' +
        '            <div class="layui-timeline-content layui-text">' +
        '                <h3 class="layui-timeline-title">笔记封面</h3>' +
        '            </div>' +
        '        </li>' +
        '   </ul>' +
        '</div>'
    ,
    messagePage:
        '<div id="msg-body" class="layui-container">' +
        '   <div class="layui-row layui-col-space10">' +
        '       <div id="msg-info" class="layui-col-md9 animated fadeInUp">' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <slot name="post"></slot>' +
        '           </div>' +
        '           <div class="layui-collapse layui-panel layui-article">' +
        '               <slot name="comment"></slot>' +
        '           </div>' +
        '       </div>' +
        '       <div class="layui-col-md3">' +
        '           <div id="affix-side">' +
        '               <slot name="info"></slot>' +
        '               <slot name="search"></slot>' +
        '               <slot name="cate"></slot>' +
        '               <slot name="tags"></slot>' +
        '           </div>' +
        '       </div>' +
        '   </div>' +
        '</div>'

    ,
    bottomNav: '' +
        '<div data-pushbar-id="bottomNav" class="pushbar from_bottom">' +
        '    <div class="layui-container" style="padding-top: 20px;padding-bottom: 15px;">' +
        '        <fieldset class="layui-elem-field layui-field-title">' +
        '            <legend style="text-align: center;font-size: 16px;">导航按钮</legend>' +
        '        </fieldset>' +
        '        <p class="layui-breadcrumb" lay-separator="|" style="text-align: center;margin-top: 10px;">' +
        '          <a href="/index">{{params.menu_home}}</a>' +
        '          <a href="/note" v-if="params.menu_note_show == 1">{{params.menu_note}}</a>' +
        '          <a href="/project" v-if="params.menu_project_show == 1">{{params.menu_project}}</a>' +
        '          <a href="/profile" v-if="params.menu_mine_show == 1">{{params.menu_mine}}</a>' +
        '          <a href="javascript:;" v-if="params.menu_search_show == 1">{{params.menu_search}}</a>' +
        '          <a href="/link" v-if="params.menu_link_show == 1">{{params.menu_link}}</a>' +
        '        </p>\n' +
        '        <ul class="layui-timeline" style="margin-top: 10px;padding-top: 15px;margin-bottom: 30px;border-top: 1px dotted #EEEEEE;">' +
        '            <li class="layui-timeline-item">' +
        '                <i class="layui-icon layui-timeline-axis">&#xe63f;</i>' +
        '                <div class="layui-timeline-content layui-text">' +
        '                    <h3 class="layui-timeline-title" style="font-size: 16px;">博客信息</h3>' +
        '                    <p style="padding-bottom: 5px;">笔记博客（NoteLofter）：是专门为 Java 程序员提供的一套开源的笔记/博客平台，同时也可作为初学者的上手学习项目。</p>' +
        '                    <p style="border-top: 1px solid #f5f5f5;padding-top: 5px;padding-bottom: 5px;">' +
        '                        交流讨论：<i class="fa fa-qq"></i>' +
        '                        <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=7bd7c01acf4e6124bfaab5a6d10504b917d5292f029b66887d00006a16a00930">' +
        '                            697053454' +
        '                        </a>。' +
        '                    </p>' +
        '                    <p style="border-top:1px solid #F5F5F5;padding-top: 5px;">' +
        '                        项目源码：<i class="fa fa-github-alt"></i>' +
        '                        <a target="_blank" href="https://github.com/miyakowork/noteblogv4">miyakowork/noteblogv4</a>。' +
        '                    </p>' +
        '                </div>' +
        '            </li>' +
        '            <li class="layui-timeline-item">' +
        '                <i class="layui-icon layui-timeline-axis">&#xe63f;</i>' +
        '                <div class="layui-timeline-content layui-text">' +
        '                    <h3 class="layui-timeline-title" style="font-size: 16px;">作者</a> </h3>' +
        '                    <p>电子邮箱：<i class="fa fa-envelope"></i> <a href="#">wuwenbinwork#163.com</p>' +
        '                    <p>个人网站：<i class="fa fa-link"></i> <a href="https://wuwenbin.me">https://wuwenbin.me</p>' +
        '                </div>' +
        '            </li>' +
        '        </ul>' +
        '    </div>' +
        '</div>'
};

Vue.component('bmy-footer', {
    template: template.footer
    , props: ['words']
});

Vue.component('bottom-nav', {
    template: template.bottomNav
    , props: ['params', 'layuiElement']
    , mounted: function () {
        this.layuiElement.render();
        new Pushbar({
            blur: true,
            overlay: true
        });
    }
});

Vue.component('bmy-header-mini', {
    template: template.headerNoTxt
    , props: {
        params: {
            type: Object
            , default: {}
        }
        , home: {
            type: Boolean
            , default: false
        }
        , note: {
            type: Boolean
            , default: false
        }
        , mine: {
            type: Boolean
            , default: false
        }
        , project: {
            type: Boolean
            , default: false
        }
        , search: {
            type: Boolean
            , default: false
        }
        , st: {
            type: Boolean
            , default: false
        }
        , title: {
            type: String
            , default: ""
        }
        , bodyid: {
            type: String
            , default: "main-body"
        },
        showSmall: {
            type: Boolean
            , default: true
        }
    }
    , data: function () {
        return {
            show: false
        }
    }
    , methods: {
        headerScroll: function () {
            var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            this.show = scrollTop > 175;
            if (this.st) {
                $("a.logo").removeClass("flipInX");
                var minWidth = $("#blog-info").find(".layui-article:eq(0)>.layui-colla-item").width();
                minWidth = minWidth <= 400 ? 280 : minWidth;
                $("#title").css("min-width", minWidth).css("font-weight", "500");
                if (this.show) {
                    $(".logo").slideUp("fast", function () {
                        $("#title").slideDown("fast");
                    })
                } else {
                    $("#title").slideUp("fast", function () {
                        $(".logo").slideDown("fast");
                    })
                }
            }
            if ($(".logo").is(":visible") && $("#title").is(":visible")) {
                $(".logo").hide();
            }
        },
        miniHeader: function () {
            var $body = $("#" + this.bodyid);
            window._justResult = $body.offset().top - $(window).scrollTop();
            BMY.animateNav($body);
            $(window).scroll(function () {
                window._justResult = BMY.animateNav($body);
            });
        },
        miniHeaderNavBtn: function () {
            var __navBtnAIndex;
            $(".simple .nav-btn a[data-title]").hover(function () {
                var that = this;
                __navBtnAIndex = window.layer.tips($(this).attr("data-title"), that, {
                    tips: [3, '#F44336'],
                    zIndex: 19930917
                });
            }, function () {
                window.layer.close(__navBtnAIndex);
            })
        }
        , searchDialog: function () {
            //搜索
            layer.open({
                type: 1
                , title: false
                , closeBtn: false
                //,shade: [0.1, '#fff']
                , shadeClose: true
                , maxWidth: 10000
                , skin: 'fly-layer-search'
                , content: ['<form action="/index" method="get">'
                    , '<input autocomplete="off" placeholder="搜索文章，回车跳转" type="text" name="s">'
                    , '</form>'].join('')
            })
        }
    }
    , mounted: function () {
        window.addEventListener("scroll", this.headerScroll);
        this.miniHeader();
        this.miniHeaderNavBtn();
    }
});

Vue.component('bmy-header', {
    template: template.header
    , props: {
        params: {
            type: Object
            , default: {}
        }
        , home: {
            type: Boolean
            , default: false
        }
        , note: {
            type: Boolean
            , default: false
        }
        , mine: {
            type: Boolean
            , default: false
        }
        , search: {
            type: Boolean
            , default: false
        }
        , st: {
            type: Boolean
            , default: false
        }
        , title: {
            type: String
            , default: ""
        }
    }
    , data: function () {
        return {
            show: false
        }
    }
    , methods: {
        headerScroll: function () {
            var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            this.show = scrollTop > 175;
            if (this.st) {
                $("a.logo").removeClass("flipInX");
                var minWidth = $("#blog-info").find(".layui-article:eq(0)>.layui-colla-item").width();
                minWidth = minWidth <= 400 ? 280 : minWidth;
                $("#title").css("min-width", minWidth).css("font-weight", "500");
                if (this.show) {
                    $(".logo").slideUp("fast", function () {
                        $("#title").slideDown("fast");
                    })
                } else {
                    $("#title").slideUp("fast", function () {
                        $(".logo").slideDown("fast");
                    })
                }
            }
            if ($(".logo").is(":visible") && $("#title").is(":visible")) {
                $(".logo").hide();
            }
        }
    }
    , mounted: function () {
        window.addEventListener("scroll", this.headerScroll);
    }
});

Vue.component('bmy-block', {
    template: template.block
    , props: {
        quote: {
            type: Object
            , default: {
                blogCount: 0
                , noteCount: 0
                , searchCount: 0
                , fileCount: 0
                , showBlog: false
                , showSearch: false
                , showNote: false
                , showClock: false
                , showFile: false
                , showText: false
                , text: ''
            }
        }
    }

});

Vue.component('bmy-articles', {template: template.articles});
Vue.component('bmy-list', {
    template: template.article
    , props: ['item', 'tags']
});

Vue.component('bmy-info', {
    template: template.info
    , props: {
        utext: String
        , qq: Number
        , info: {
            type: Boolean
            , default: true
        }
        , su: {
            type: Object
            , default: {}
        }
        , order: Number
    }
    , data: function () {
        return {
            layerIndex: null
        }
    }
    , methods: {
        tip: function () {
            layer.msg('正在通过QQ登入', {icon: 16, shade: 0.1, time: 0})
        }
        , tipsOver: function () {
            this.layerIndex = layer.tips('点击注销', '#logout', {
                tips: [3, '#000']
            })
        }
        , tipsOut: function () {
            layer.close(this.layerIndex);
        }
    }
});

Vue.component('bmy-search', {
    template: template.search
    , props: ['sw']
    , data: function () {
        return {
            words: ''
        }
    }
    , mounted: function () {
        this.words = this.sw;
    }
    , methods: {
        updateValue: function (value) {
            this.words = value;
            this.$emit("input", value);
        }
        , searchAll: function () {
            var s = this.words;
            s = s !== undefined ? s : "";
            location.href = s === "" ? "/" : "/index?s=" + s;
        }
        , aboutSearch: function () {
            layer.msg("仅做标题和文章原始内容关键字匹配，tag搜索不包含在内！");
        }
    }
});

Vue.component('bmy-cate', {
    template: template.cate
    , props: ['cates']
    , methods: {
        cateUrl: function (c) {
            return "/index?c=" + c.id;
        }
    }
});

Vue.component('bmy-random', {
    template: template.random
    , props: ['articles']
    , methods: {
        subTitle: function (title) {
            var len = 21;
            return BMY.lenStat2(title) < len ? title : title.substring(0, len).concat("...");
        }
    }
});

Vue.component('bmy-tab', {
    template: template.tab
    , props: ['tabs']
    , methods: {
        find: function (name) {
            location.href = "/index?t=" + name;
        }
    }
});

Vue.component('bmy-article-page', {
    template: template.articlePage
    , data: function () {
        return {
            approve: 0
        }
    }
    , props: ['article', 'author', 'name', 'comments', 'tags', 'similars', "alipay", "wechat", "su"]
    , computed: {
        postDate: function () {
            return BMY.dateFormatter(this.article.post, "-");
        }
        , url: function () {
            return location.href;
        }
    }
    , mounted: function () {
        this.approve = this.article.approveCnt
    }
    , methods: {
        money: function (alipay, wechat) {
            alipay = alipay === null || alipay === undefined || alipay === "" ? "/static/assets/img/noqrcode.jpg" : alipay;
            wechat = wechat === null || wechat === undefined || wechat === "" ? "/static/assets/img/noqrcode.jpg" : wechat;
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['640px', '300px'],
                shadeClose: true,
                skin: 'text-center',
                content:
                    '<div class="layui-fluid">' +
                    '   <div class="layui-row layui-mt20">' +
                    '       <div class="layui-col-md6">' +
                    '           <img src="' + wechat + '" style="height: 250px;width: 250px;">' +
                    '           <p class="text-center">微信</p>' +
                    '       </div>' +
                    '       <div class="layui-col-md6">' +
                    '           <img src="' + alipay + '" style="height: 250px;width: 250px;">' +
                    '           <p class="text-center">支付宝</p>' +
                    '       </div>' +
                    '   </div> ' +
                    '</div>'
            });
        }
        , emotion: function () {
            var that = this;
            var uid = that.su !== null ? that.su.id : "guest";
            if (BMY.getCookie("article::" + that.article.id + "::" + uid) != null) {
                layer.msg("近期您已经点过赞，感谢您的支持！");
            } else {
                $.post("/article/approve", {articleId: this.article.id}, function (json) {
                    if (json.code === BMY.status.ok) {
                        BMY.setCookie("article::" + that.article.id + "::" + uid, "noteblog system");
                        that.approve++;
                        layer.msg("谢谢您的支持！");
                    }
                })
            }
        }
    }
});

Vue.component('bmy-article-page-mini', {
    template: template.articlePageMini
    , data: function () {
        return {
            approve: 0
        }
    }
    , props: ['article', 'author', 'name', 'comments', 'tags', 'similars', "alipay", "wechat", "su"]
    , computed: {
        postDate: function () {
            return BMY.dateFormatter(this.article.post, "-");
        }
        , url: function () {
            return location.href;
        }
        , isRichTxt: function () {
            return this.article.mdContent == null || this.article.mdContent === "";
        }
    }
    , mounted: function () {
        this.approve = this.article.approveCnt;
        editormd.markdownToHTML("doc-content", {
            markdown: this.article.mdContent,//+ "\r\n" + $("#append-test").text(),
            //htmlDecode      : true,       // 开启 HTML 标签解析，为了安全性，默认不开启
            htmlDecode: "style,script,iframe",  // you can filter tags decode
            tocContainer: "#custom-toc-container", // 自定义 ToC 容器层
            //gfm             : false,
            //tocDropdown     : true,
            markdownSourceCode: false, // 是否保留 Markdown 源码，即是否删除保存源码的 Textarea 标签
            emoji: false,
            taskList: true,
            tex: true,  // 默认不解析
            flowChart: true,  // 默认不解析
            sequenceDiagram: true// 默认不解析
        });
    }
    , methods: {
        money: function (alipay, wechat) {
            alipay = alipay === null || alipay === undefined || alipay === "" ? "/static/assets/img/noqrcode.jpg" : alipay;
            wechat = wechat === null || wechat === undefined || wechat === "" ? "/static/assets/img/noqrcode.jpg" : wechat;
            layer.open({
                type: 1,
                title: false,
                closeBtn: 0,
                area: ['640px', '300px'],
                shadeClose: true,
                skin: 'text-center',
                content:
                    '<div class="layui-fluid">' +
                    '   <div class="layui-row layui-mt20">' +
                    '       <div class="layui-col-md6">' +
                    '           <img src="' + wechat + '" style="height: 250px;width: 250px;">' +
                    '           <p class="text-center">微信</p>' +
                    '       </div>' +
                    '       <div class="layui-col-md6">' +
                    '           <img src="' + alipay + '" style="height: 250px;width: 250px;">' +
                    '           <p class="text-center">支付宝</p>' +
                    '       </div>' +
                    '   </div> ' +
                    '</div>'
            });
        }
        , emotion: function () {
            var that = this;
            var uid = that.su !== null ? that.su.id : "guest";
            if (BMY.getCookie("article::" + that.article.id + "::" + uid) != null) {
                layer.msg("近期您已经点过赞，感谢您的支持！");
            } else {
                $.post("/article/approve", {articleId: this.article.id}, function (json) {
                    if (json.code === BMY.status.ok) {
                        BMY.setCookie("article::" + that.article.id + "::" + uid, "noteblog system");
                        that.approve++;
                        layer.msg("谢谢您的支持！");
                    }
                })
            }
        }
    }
});

Vue.component('bmy-similar', {
    template: template.similar
    , props: ['articles']
    , methods: {
        subTitle: function (title) {
            var len = 21;
            return BMY.lenStat2(title) < len ? title : title.substring(0, len).concat("...");
        }
    }
});

Vue.component('bmy-comment', {
    template: template.comment
    , props: {
        su: {
            type: Object
            , default: {}
        },
        id: {
            type: String
        }
    }
    , methods: {
        submit: function (articleId) {
            var h1 = BMY.tempHtml === undefined ? "" : BMY.tempHtml;
            var h2 = BMY.tempHtml2 === undefined ? "" : BMY.tempHtml2;
            var comment = h1 + "<p>" + BMY.layedit.getContent(BMY.editor).replace(h2, "") + "</p>";
            if (comment === "<p></p>") {
                layui.layer.msg("请填写评论内容");
            } else {
                $.post("/token/comment/sub", {
                    articleId: articleId,
                    userId: this.su.id,
                    user: this.su.id,
                    enable: true,
                    comment: comment
                }, function (resp) {
                    layer.msg(resp.message);
                    setTimeout(function () {
                        if (resp.code === BMY.status.ok) {
                            location.href = "/article/" + articleId + "?_" + new Date().getTime() + "#cta"
                        }
                    }, 1000);
                });
            }
        }
        , beforeLogin: function () {
            var $btn = $("#beforeLogin");
            $btn.addClass("layui-btn-disabled");
            $btn.text("请刷新页面..")
        }
    }
});

Vue.component('bmy-msg-comment', {
    template: template.messageComment
    , props: {
        su: {
            type: Object
            , default: {}
        }
    }
    , methods: {
        submit: function () {
            var h1 = BMY.tempHtml === undefined ? "" : BMY.tempHtml;
            var h2 = BMY.tempHtml2 === undefined ? "" : BMY.tempHtml2;
            var comment = h1 + "<p>" + BMY.layedit.getContent(BMY.editor).replace(h2, "") + "</p>";
            if (comment === "<p></p>") {
                layui.layer.msg("请填写评论内容");
            } else {
                $.post("/token/msg/sub", {
                    userId: this.su.id,
                    user: this.su.id,
                    comment: comment
                }, function (resp) {
                    layer.msg(resp.message);
                    setTimeout(function () {
                        if (resp.code === BMY.status.ok) {
                            location.href = "/msg"
                        }
                    }, 1000);
                });
            }
        }
        , beforeLogin: function () {
            var $btn = $("#beforeLogin");
            $btn.addClass("layui-btn-disabled");
            $btn.text("请刷新页面...")
        }
    }
});

Vue.component('bmy-comment-list', {
    template: template.commentArea
    , props: {
        comments: {
            type: Object
            , default: {}
        }
        , tips: {
            type: String
            , default: {}
        }
        , re: {
            type: Boolean
            , default: false
        }
    }
    , methods: {
        masterColor: function (id) {
            return id === 1 ? "color:#FF5722;" : "";
        }
        , postDt: function (d) {
            return BMY.wholeCnDate(d);
        }
        , reback: function (name, content) {
            BMY.tempHtml =
                '<blockquote class="layui-elem-quote layui-quote-nm">' +
                '   <a href="javascript:;" class="re">回复@' + name + '</a>：' + content +
                '</blockquote>';
            BMY.tempHtml2 =
                '<a href="javascript:;" class="re">回复@' + name + '</a>：';
            $(window.frames["LAY_layedit_1"].document).find("body").html(BMY.tempHtml2);
            location.href = "#cta";
        }
    }
});

Vue.component('bmy-simple-comment-list', {
    template: template.simpleCommentArea
    , props: {
        comments: {
            type: Object
            , default: {}
        }
        , tips: {
            type: String
            , default: {}
        }
        , re: {
            type: Boolean
            , default: false
        }
    }
    , methods: {
        masterColor: function (id) {
            return id === 1 ? "color:#FF5722;" : "";
        }
        , postDt: function (d) {
            return BMY.wholeCnDate(d);
        }
        , reback: function (name, content) {
            BMY.tempHtml =
                '<blockquote class="layui-elem-quote layui-quote-nm">' +
                '   <a href="javascript:;" class="re">回复@' + name + '</a>：' + content +
                '</blockquote>';
            BMY.tempHtml2 =
                '<a href="javascript:;" class="re">回复@' + name + '</a>：';
            $(window.frames["LAY_layedit_1"].document).find("body").html(BMY.tempHtml2);
            location.href = "#cta";
        }
    }
});

Vue.component('bmy-notes', {
    template: template.notePage
    , props: ['su', 'sw']
    , data: function () {
        return {
            words: ''
        }
    }
    , mounted: function () {
        this.words = this.sw;
    }
    , methods: {
        updateValue: function (value) {
            this.words = value;
            this.$emit("input", value);
        }
        , searchAll: function () {
            var s = this.words;
            s = s !== undefined ? s : "";
            location.href = s === "" ? "/note" : "/note?t=" + s + "&cc=" + s;
        }
    }
});

Vue.component('bmy-msg-page', {
    template: template.messagePage
});