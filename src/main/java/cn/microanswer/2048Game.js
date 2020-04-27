"use strict";
(function (window) {
    // 要随机产生的数都是来自这个数组。 (tv：得到这个数的概率)
    var numConfig = [
        {value: 4, color: '#ffd85b', tv: 0.1},
        {value: 2, color: '#ff9917', tv: 0.9}
        ];

    // 游戏实例对象。
    var game = {

        // 初始化方法
        init: function () {

            // 初始化游戏视图。
            if (!game.doms.canvasDom) {
                game.doms.canvasDom = document.createElement("canvas");
                game.methods.initCanvasStyle(game.doms.canvasDom);
                document.body.appendChild(game.doms.canvasDom);
            }

            // 初始化游戏相关东西
            game.methods.initGame();

            // 绘制游戏
            game.methods.drawGame();
        },

        // 视图画布区域
        doms: {
            canvasDom: null
        },

        // 数据区域
        datas: {
            Constant_viewSize: 300, // 定义游戏区域大小。
            Constant_spaceWidth: 8, // 定义间隙宽度。
            Constant_backgroundColor: '#4b4b4b', // 定义背景颜色。
            Constant_spaceColor: '#ffbe7a', // 定义网格线颜色。
            Constant_count: 4, // 定义游戏运行的行列数

            itemSize: -1, // 定义每个数字大小。
            items: null, // 定义方块集合。所有方块将放在这里

            ctx: null
        },

        // 所有的方法。
        methods: {

            // 初始化画布的样式。
            initCanvasStyle: function (canvasDom) {
                canvasDom.style.display = 'block';
                canvasDom.style.height = game.datas.Constant_viewSize + 'px';
                canvasDom.style.width = game.datas.Constant_viewSize + 'px';
                canvasDom.style.borderRadius = game.datas.spaceWidth + 'px';
                canvasDom.style.margin = '100px auto 0 auto';
                canvasDom.width = game.datas.Constant_viewSize;
                canvasDom.height = game.datas.Constant_viewSize;
            },

            // 初始化游戏相关参数。
            initGame: function () {

                // 计算出每个数字方块的大小。 等于 = (游戏区域 - (5 x 间隙宽度))/4
                game.datas.itemSize =
                    Math.round(
                        (game.datas.Constant_viewSize -
                            ((game.datas.Constant_count + 1) * game.datas.Constant_spaceWidth)
                        ) / game.datas.Constant_count);

                // 初始化 绘制画板。
                game.datas.ctx = game.doms.canvasDom.getContext('2d');
                game.datas.ctx.width = game.datas.Constant_viewSize;
                game.datas.ctx.height = game.datas.Constant_viewSize;
                game.datas.ctx.lineJoin = 'round';
                game.datas.ctx.lineCap = 'round';
                game.datas.ctx.font = '35px Arial';

                // 随机初始化 2 个方块。
                game.datas.items = [];
                var block1 = game.methods.__makeARandomBlock(game.datas.Constant_count, game.datas.itemSize);
                var block2 = game.methods.__makeARandomBlock(game.datas.Constant_count, game.datas.itemSize);

                for (var row = 0; row < game.datas.Constant_count; row++) {
                    game.datas.items.push([]);
                    for (var col = 0; col < game.datas.Constant_count; col++) {
                        game.datas.items[row].push(null);
                    }
                }

                game.datas.items[block1.row][block1.col] = block1;
                game.datas.items[block2.row][block2.col] = block2;
            },

            /**
             * 随机产生一个方块。
             * @param limit 产生的范围， 0~limit
             * @param size  方块大小。
             * @private
             */
            __makeARandomBlock: function (limit, size) {
                var block = {
                    num: 0,
                    row: -1,
                    col: -1,
                    size: size,
                    color: '下方将更具数字设定颜色。',
                    // 绘制自身方块。
                    draw: function (ctx) {

                        // 绘制方块背景
                        var x = block.row * (game.datas.Constant_spaceWidth + block.size) + game.datas.Constant_spaceWidth;
                        var y = block.col * (game.datas.Constant_spaceWidth + block.size) + game.datas.Constant_spaceWidth;
                        game.methods.__drawRoundRect(ctx, x, y, block.size, block.size, game.datas.Constant_spaceWidth, block.color);

                        // 绘制数字
                        var fs = ctx.fillStyle;
                        ctx.fillStyle = '#FFFFFF';
                        var tw = ctx.measureText(block.num + '').width;
                        ctx.fillText(block.num, x + ((block.size - tw)/2), y + ((block.size + 25)/2), block.size);

                        ctx.fillStyle = fs;
                    }
                };

                // 根据各数的概率产生一个数用于此方块。
                var r = Math.random();
                var flag = 0;
                for (var index = 0; index < numConfig.length; index++) {
                    var num = numConfig[index];
                    var fl2 = (num.tv + flag);
                    if (flag <= r && r < fl2) {
                        block.num = num.value;
                        block.color = num.color;
                        break;
                    }
                    flag += num.tv;
                }

                if (block.num <= 0) {
                    throw new Error('产生数字失败。可能数字配置总和不为 1。' + r);
                }

                // (Math.random() * (y - x)) + x ==> [0, y-x) ==> [x, y)
                block.row = Math.floor(Math.random() * limit);
                block.col = Math.floor(Math.random() * limit);

                return block;
            },

            // 绘制游戏
            drawGame: function () {
                var datas = game.datas;
                var ctx = datas.ctx;

                // 绘制背景色。
                ctx.fillStyle = datas.Constant_backgroundColor;
                game.methods.__drawRoundRect(ctx,
                    datas.Constant_spaceWidth / 2,
                    datas.Constant_spaceWidth / 2,
                    datas.Constant_viewSize - datas.Constant_spaceWidth,
                    datas.Constant_viewSize - datas.Constant_spaceWidth,
                    datas.Constant_spaceWidth,
                    datas.Constant_backgroundColor);

                // 绘制网格
                game.methods.__drawSpaceGrid(ctx, datas.Constant_spaceWidth, datas.Constant_spaceColor);

                // 绘制方块。
                game.methods.__drawBlock(ctx, datas.items);
            },

            // 圆角矩形绘制。
            __drawRoundRect: function (ctx, x, y, width, height, radius, color, isStroke, strokeSize) {
                var fs = ctx.fillStyle;
                var ss = ctx.strokeStyle;
                var linw = ctx.lineWidth;
                ctx.fillStyle = color;
                ctx.strokeStyle = color;
                ctx.lineWidth = strokeSize;
                ctx.beginPath();
                ctx.bezierCurveTo(x, y + radius, x, y, x + radius, y);
                ctx.lineTo(x + width - radius, y);
                ctx.bezierCurveTo(x + width - radius, y, x + width, y, x + width, y + radius);
                ctx.lineTo(x + width, y + height - radius);
                ctx.bezierCurveTo(x + width, y + height - radius, x + width, y + height, x + width - radius, y + height);
                ctx.lineTo(x + radius, y + height);
                ctx.bezierCurveTo(x + radius, y + height, x, y + height, x, y + height - radius);
                ctx.closePath();
                isStroke ? ctx.stroke() : ctx.fill();
                ctx.fillStyle = fs;
                ctx.strokeStyle = ss;
                ctx.lineWidth = linw;
            },
            // 绘制网格
            __drawSpaceGrid: function (ctx, spaceWidth, spaceColor) {
                var datas = game.datas;

                // 先绘制边框。
                game.methods.__drawRoundRect(ctx,
                    spaceWidth / 2,
                    spaceWidth / 2,
                    game.datas.Constant_viewSize - spaceWidth,
                    game.datas.Constant_viewSize - spaceWidth,
                    spaceWidth,
                    spaceColor,
                    true,
                    spaceWidth);

                // 后绘制内部的线条。
                var firstRowStartX = 0;
                var firstRowStartY = spaceWidth + datas.itemSize;
                ctx.beginPath();
                for (var row = 1; row < datas.Constant_count; row++) {
                    var y = (firstRowStartY * row) + (spaceWidth / 2.0);
                    ctx.moveTo(firstRowStartX, y);
                    ctx.lineTo(firstRowStartX + datas.Constant_viewSize, y);
                }
                var firstColStartX = spaceWidth + datas.itemSize;
                var firstColStartY = 0;
                for (var col = 1; col < datas.Constant_count; col++) {
                    var x = (firstColStartX * col) + (spaceWidth / 2.0);
                    ctx.moveTo(x, firstColStartY);
                    ctx.lineTo(x, firstColStartY + datas.Constant_viewSize);
                }
                ctx.closePath();
                var ss = ctx.strokeStyle;
                var wl = ctx.lineWidth;
                ctx.strokeStyle = spaceColor;
                ctx.lineWidth = spaceWidth;
                ctx.stroke();

                ctx.strokeStyle = ss;
                ctx.lineWidth = wl;
            },
            // 绘制方块
            __drawBlock: function (ctx, items) {
                var ss = ctx.strokeStyle;
                var lw = ctx.lineWidth;
                var fc = ctx.fillStyle;

                for (var row = 0; row < items.length; row++) {
                    var rows = items[row];
                    for (var col = 0; col < rows.length; col++) {
                        var block = rows[col];
                        if (block) {
                            block.draw(ctx);
                        }
                    }
                }


                ctx.strokeStyle = ss;
                ctx.lineWidth = lw;
                ctx.fillStyle = fc;
            },

            /**
             * 滚动。
             * @param from 从某个值
             * @param to 到某个值
             * @param back 不停的回调
             */
            __scroll: function (from, to, back) {
                if (window._sclin_) {
                    // 滚动还没有完成，不执行
                    return;
                }
                var y = from;
                y = y || 0;
                var startY = y;

                var distanceY = to - startY;

                if (distanceY === 0) {
                    // 没有意义的滚动
                    back && back.end && back.end(to);
                    return undefined
                }

                var ended = false;
                var time = 350;
                var ftp = 60;
                var ease = function (pos) {
                    return -(Math.pow((pos - 1), 4) - 1);
                }; // 要使用的缓动公式
                var startTime = new Date().getTime(); // 开始时间
                // 开始执行
                (function dd() {
                    setTimeout(function () {
                        window._sclin_ = true;
                        var now = new Date().getTime(); // 当前帧开始时间
                        var timestamp = now - startTime; // 逝去的时间(已进行动画的时间)
                        var detal2 = ease(timestamp / time);
                        var result2 = Math.ceil(startY + detal2 * distanceY);
                        if (!ended) {
                            back && back.goo && back.goo(result2, to);
                        }
                        if (time <= timestamp) {
                            ended = true;
                            back.end(to);
                            window._sclin_ = false;
                        } else {
                            setTimeout(dd, 1000 / ftp);
                        }
                    }, 1000 / ftp);
                })();
            }
        }
    };

    game.init();

})(window);
