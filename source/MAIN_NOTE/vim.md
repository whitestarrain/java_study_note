# 1. 基础知识

> 裸vim

- 切换：
	- esc
	- C-[

- 移动
	- hjkl:移动
	- w:下一个单词开头，e：下一个单词末尾。W下一个空格开头，E下一个空格末尾
	- % 括号匹配移动
	- gg移动到开头，G移动到末尾，数字+gg移动到指定行 
	- $ 到行末;0到行首;^ 到第一个不是空格的位置(可以使用0w代替)
	- M(middle)移动到屏幕中间位置。H(Head)移动到屏幕顶部位置。L(Lower)移动到屏幕底部位置
	- zz将编辑行移动到屏幕中间,zt将编辑行移动到屏幕顶部，zb将编辑行移动到屏幕底部
	- t, 到逗号前的一个字符，逗号可以变成其他字符。T同理，反方向。然后通过分号;跳转到下一个，逗号，跳转到上一个(until)
	- f,到达第一个逗号的位置，逗号可以变成其他字符。F同理，反方向。然后通过分号;跳转到下一个，逗号，跳转到上一个(find)
	- C-i C-o新位置，旧位置
	- -(不按shift)到上一行第一个非空格字符处。+(按着shift)到下一行第一个非空字符处
	- C-b,C-f(forword)上下翻页
	- C-u(upword),C-d 半页
	- gi跳转到最后一编辑位置并进入插入模式
	- g_到最后一个不是空格的位置,可以代替$,
	- ()在句子间移动。{}在段落间移动。（不用记，之后有easy-motion插件）

- 搜索：
	- /pattern 搜索，n往下搜索，N往上搜索。?pattern倒序搜索
	- `* #` 查询匹配当前光标所在单词，前后移到查到的单词

- 可视化选择
	- v
	- V
	- C-v

- 编辑
	- i,a,I,A插入。A可以代替$,I可以代替^
	- o 在光标后插入一行，O在光标前插入一行
	- x删除一个，X往左删除
	- c删除后面指定范围并切到插入模式，cc从开头一直删除到行末并进入插入模式，C一直删到行末并切到插入模式(change)
	- s删除一个并切换到插入模式(xi)，S删除一行并切换到插入模式(和cc功能相同)(substitute)
	- d删除后面指定范围，D一直删除到行末
		> 比如dt,一直删除到逗号那里,逗号不会删
	- daw(delete around word)删除当前光标所在单词,同理还有caw。详情请看后面区域选择及其原理
	- r替换一个，R不断切换后面字符
	- u回退，U回退一行的操作;C-r取消回退，C-R取消回退一行
	- dd删除一行
	- J把后面一行和移到当前所在行
	- = 自动给缩进，相当于格式化
	- yy 复制一行，p粘贴
	- 0y$ 从行开头复制到行末尾；y2/help复制查到的两个help之间的内容
	- gU选中内容变大写,gu选中内容变小写

- 插入模式下的编辑操作：
	> 终端中编辑适用
	- C-h删除上一个字符
	- C-w删除上一个单词
	- C-u删除当前行

- 仅终端使用：
	- C-a快速移动到开头
	- C-e快速移动到结尾
	- C-b前移
	- C-f后移

- 替换
	> 本质上就是 substitute这个命令
	- 格式[range]s/pattern/string/[flags]
		- flag:
			- g:全局
			- c:进行确认
	- 注意：也支持正则表达式
		- 例子：为非空行添加双引号  `:% s/^\(\S+\)$/"\1"/g`
	- 方式：
		- 在一行内替换头一个字符串 old 为新的字符串 new，请输入  :s/old/new
		- 在一行内替换所有的字符串 old 为新的字符串 new，请输入  :s/old/new/g
		- 在两行内替换所有的字符串 old 为新的字符串 new，请输入  :#,#s/old/new/g
		- 在文件内替换所有的字符串 old 为新的字符串 new，请输入  :%s/old/new/g
		- 进行全文替换时询问用户确认每个替换需添加 c 标志        :%s/old/new/gc
	- 选项：
		- y - yes 替换
		- n - no 不替换
		- a - all 替换所有
		- q - quit 退出替换
		- l - last 最后一个，并把光标移动到行首
		- ^E 向下滚屏
		- ^Y 向上滚屏
- 其他
	- df 查看光标所在字符串对应文件
	- gd 查看定义位置
	- C-p C-n 代码提示上下浏览
	- dh 相当于将鼠标移动到那里（vscode专属）	
	- C-w h 光标移动到左侧目录树（vscode专属）

- 多文件操作
	- :Vexplore 纵向展示目录树（）
	- :e path/file 打开指定文件
	- 缓冲区：每打开一个文件就是一个缓冲区，用来存储文件修改
		- :ls 列举缓冲区。
		- :b 2 跳转到第2个缓冲区。:bpre :bnext :bfirst :blast 也可以进行跳转
		- :b name 进行tab补全，跳转到缓冲区
	- 窗口：一个缓冲区可以分割为多个窗口,一个窗口也可以打开不同的缓冲区
		- :sp横向增加分屏(或者C-w s)，:vsp纵向增加分屏(或者C-w v)。后面也可以跟路径和文件名，用来指定分屏窗口打开哪个文件
		- C-w w切换到下一个窗口，C-w r互换窗口，C-w c 关闭当前窗口，C-w o 关闭其他窗口
		- C-w hjkl 左下上右选择窗口。C-w w窗口间循环切换
		- C-w HJKL 将当前窗口移动到左下上右侧
		- C-w <> 左右移动分割线，C-w =平分空间，C-w _最大化活动窗口高度，C-w |最大化互动窗口宽度
	- tab:容纳一系列窗口的容器。类似工作区。用起来比较少
		- 这里不多说了，看后面的插件

- 区域选择 `<action>a<object>` 或 `<action>i<object>`
	> action可以是任何的命令，如 d (删除), y (拷贝), v (可以视模式选择)。c(修改)，s(替代)<br>
	> object 可能是： w 一个单词， W 一个以空格为分隔的单词， s 一个句字， p 一个段落。也可以是一个特别的字符："、 '、 )、 }、 ]。<br>
	> i 表示不会选中分界，a表示会选中分界，t表示直到，而不会选择前面的
	- 假设你有一个字符串 (map (+) ("foo")).而光标键在第一个 o 的位置。
	- vi" → 会选择 foo.
	- va" → 会选择 "foo".
	- vt" → 会选择 oo
	- vi) → 会选择 "foo".
	- va) → 会选择("foo").
	- v2i) → 会选择 map (+) ("foo")
	- v2a) → 会选择 (map (+) ("foo"))
	- v2t) → 会选择 oo")

> 推荐使用文本对象来提高编辑速度，而不是单纯的退格键※

- 文本对象（区域选择命令原理解析）
	- `[number]<command>[text object]`
		- number 表示次数
		- command表示命令 d c y v
		- text object 表示要操作的文本对象 如单词w，句子s，段落p
	- 区域选择文本对象示例：
		- iw 表示inner word ，会选中当前单词(也就是说 i 不会包含边界)
		- aw 表示 a word，不但会选中当前单词，也会包含单词后的空格(也就是说 a 会包含边界)
		- a" 表示双引号里面的内容，包含双引号 - i" 表示双引号里面的内容，不好寒双引号
		- 2a" 表示选择两层双引号，包括双引号里面的内容
	- 完整示例:
		- daw 删除一个单词
		- das 删除一个句子
		- dap 删除一个段落

- 复制粘贴，寄存器
	- 简单命令
		- 普通模式下：
			- y (yank) 复制
			- p(put)粘贴
			- yy 复制一行 yiw复制一个单词 
		- 插入模式下
			- 可以通过C-v粘贴剪切板内容
			- C-r 寄存器名  粘贴指定寄存器内容
			- 复制还是到普通模式下吧
	- 剪切版复制代码缩进混乱解决办法：
		- 方式1 平时不推荐,往服务器上复制时使用
			- :set autoindent 设置自动缩进
			- :set paste 这样就能复制代码了，但会导致自动缩进失效
			- 复制代码
			- :set nopaste 恢复自动缩进
		- 方式2 通过寄存器 推荐
			- normal模式下输入 "+p
			- 注意：服务器上的vim的话，该方式就无法使用了，就要使用方式1
	- 每次删除或者赋值的内容默认都会放到无名寄存器中
	- 可以通过`"名称`指定寄存器。
		- 寄存器：
			- 无名寄存器是默认的。非要指定名称的话就是`""`
			- 常用名称是 a-z
			- 复制时除了会存到无名寄存器中外，也会存到`"0`寄存器
			- 系统剪切板 `"+`寄存器。可以复制到系统剪切板，或者从系统剪切板粘贴
				> echo has('clipboard')查看是否支持该特性。1的话就支持
			- `"%`存储当前文件名
			- `".`存储上次插入模式下插入的文本
			- set clipboard=unnamed 把系统剪切板作为默认的无名寄存器(注意，不要加多余空格)
		- 示例
			- "ayiw 把一个单词存储到a寄存器中
			- "byy 把当前所在行复制到b寄存器中
			- "ap 把寄存器a中的内容赋值
- 宏
	- 说明：宏可以看成一系列命令的集合，可以将操作进行录制后，再进行回放
	- 基础操作：
		- q开始录制宏，q结束录制宏
		- qa 将录制内容存储到寄存器a中
		- @a 调用a寄存器中的宏
	- 调用宏
		- V 进行选择要调用的内容
		- 输入 :normal @a (表示再normal模式下每行执行a寄存器里的宏)
			- 注意，也可以再normal后直接输入命令操作，@a本身就是调用存储在寄存器中的命令
- 补全（这里只说主要用的，不再系统讲）：
	- C-n,C-p补全单词
	- C-x C-f 补全文件名。
	- C-x C-o 代码补全，需要开启文档类型检查，安装插件
		- filetype on 开启文档类型检查。 set filetype 查看文档类型
		- 插件设置看后面
- 命令
	- . 重复上次命令
	- :help <command> 查看帮助
	- :wq 存储退出(ZZ同样效果),:!q强制退出，:w保存
	- :qa 退出所有窗口
	- :saveas path/file 另存为

- vim配置文件：
	- set incsearch:设置高亮搜索
	- set hlsearch:设置搜索高亮
	- syntax on:开启代码高亮

- 更换配色
	- :colorscheme 显示当前配色方案，默认是default
	- :colorscheme C-d 可以显示所有配色方案
	- 可以去 https://github.com/flazz/vim-colorschemes 安装其他配色方案
	- 持久化的话就写到 vimrc中

# 2. vim配置文件

- 目的：
	- 持久化配置，比如行号，高亮等
- vim常用设置
	- 到vimrc里面看看吧
- 位置：
	- :version可以查看配置文件目录，权重从高到低
- 按键映射
	- 目的：把一个操作映射到另一个操作
- 普通映射：(仅限普通模式)
	- map option1 option2 把option1映射到option2
		- `map <space> viw` 按空格就会选中一个单词  `<C-d> dd` ctrl+d就会删除一行
	- 取消映射：unmap
- 指定模式映射
	- normal:nmap
	- visual:vmap
	- insert:imap
		- 例：`imap <C-d> <Esc>ddi` 插入模式下ctrl+d就会 返回normal模式，删除一行，再进入插入模式
- 禁用递归映射
	> 任何时候都要使用非递归映射
	- 原因：上面的映射都会进行递归映射，一直映射到没有映射的按键。同时使用插件时也会导致很多冲突
	- 方法：
		- nnoremap
		- vnoremp
		- inoremap
- 设置leader
	```vim
	let mapleader=','
	inoremap <leader>w <Esc>:w<cr>
	" 插入模式下逗号+w  相当于  退回普通模式，输入:w 再回车
	```

# 3. vim插件

## 3.1. 插件

## 3.2. 插件安装示例：

- 插件管理器：vim-plug
	> 更多其实看github上的文档就行
	- 下载vim文件
	- 将文件放到autoload文件夹
- 安装插件vim-startify：
	- 去vimrc中进行配置：
		```vim
		call plug#begin('~/vimfiles/plugin') " 这里写的是插件放的位置

		Plug 'mhinz/vim-startify'
		" 这里写的是需要的插件
		call plug#end()
		```
	- 输入:PlugInstall
	- windows报错解决：
		- 将插件文件夹下的文件夹（仅文件夹）和vimfiles下的文件夹 合并 
		- 不要替换tag，把所有插件的tag放到一个文件夹中

## 3.3. 寻找插件：

- 大多数插件都托管在github上，google关键词搜索
- 使用网站： http://vimawesome.com/ (十分推荐)
- 浏览网上开源的vimrc配置。


## 3.4. 美化插件

- 启动界面：vim-startify
- 状态栏：vim-airline
- 代码缩进线条：indentline

- 配色：
	- hybird 黑色，类似one dark
	- solarized 对眼睛较好，亮暗两种主题
	- gruvbox 两种主题

- 配色安装：
	- 方式和插件安装相同
	- 要持久化的话要在配置中加上 colorscheme XXX
	- 简单点也可以把hybird.vim下载后放到安装目录的colors下


## 3.5. 实用插件

- 文件树安装：
	- 安装:Plug 'scrooloose/nerdtree'
	- 配置
	```vim
	" auto turn on
	" autocmd vimenter * NERDTree
	" ctrl+n to toggle file tree
	noremap <C-n> :NERDTreeToggle<CR>
	" ignore file
	let NERDTreeIgnore=[
		\ '\.pyc$','\~$','\.swp','\.git$','\.pyo$','\.svn$','\.swp$','__pycache__'
		\ ]
	nnoremap ,v :NERDTreeFind<cr>
	" and ctrl w p  to back
	```
- 模糊搜索：
	- 安装：Plug 'ctrlpvim/ctrlp.vim'
	- 配置
		```
		" ctrlp config 
		let g:ctrlp_map = '<C-p>'
		```
- 快速跳转
	- 目的：能够实现窗口任意区域的跳转
	- 安装：Plug 'easymotion/vim-easymotion'
	- 配置：
		```
		" easymotion config

		nmap ss <Plug>(easymotion-s2)
		" 使用ss时会调用s2查询

		```
- 成对编辑
	- 安装：Plug `tpope/vim-surround`
	- 使用：
		- ds：delete a surrounding
		- cs:change a surrounding
		- ys:you add a surrounding
	- 使用示例：ys iw " 为当前单词添加双引号  ys就是指令
	- 配置：不用

- 多个文件模糊搜索：
	- 安装： 挑一个
		- Ag.vim
		- fzf.vim
	- 完全可以提到 ctrlp
	- 常用命令:
		- Files
		- Ag

- 批量搜索替换
	- 安装：Plug `brooth/far.vim`
	- 使用：
		- `Far AAA XXXX **/*.py`
		- 展示预览，没问题的话；
		- `Fardo`进行替换

- golang插件
	- 安装： `Plug 'fatih/vim-go', { 'do': ':GoUpdateBinaries' }`
	- 教程看官方文档
	- ctrl+] 查看定义
	- 自动导入，重构，格式化，运行等功能都有集成
	- 使用python和php的都转向go了，推荐学学

- python-mode插件
	- 安装： Plug 'python-mode/python-mode', { 'for': 'python', 'branch': 'develop' }
	- 注意：
		- 安装时确保网络
		- vim要支持python3

- 大纲插件；
	- 安装 Plug 'majutsushi/tagbar'
	- 注意：查看文档，需要安装ctags生成tag文件
	
- 高亮指定单词：
	- 安装：Plug 'lfv89/vim-interestingwords'
	- 使用：
		- 默认在指定单词处`<leader>k`开启高亮，
		- 使用`<leader>K`清除所有高亮
		- n和N进行跳转

- 强大的代码补全插件
	- deoplete.nvm
		- 内容：通用补全，和之前的全家桶有区别
		- 安装:查看github文档，有一些vimscript,项目名称为:shougo/deoplete.nvim
		- 支持vim8异步和模糊补全
		- 注意：注意文档提出的各种要求，比如neovim，python版本
		- 配置：
			```
			" 补全时默认会有文档预览，设置这条关闭预览
			set completeopt-=preview
			```
	- coc.vim
		- 支持vim8异步。 主要特色是支持 LSP(Language Serve Propocol)
		- 使vim像vscode一样！ 我个人更喜欢

- 代码格式化和静态检查：
	- 异步检查：vim8和neovim支持，在检查时也能进行编写，不会卡住
	- 无论写哪种语言都要加上这两种，重要！！！
	- 格式化：
		- nsbdchd/neoformat
			- 注意依赖,需要安装对应的库。插件本身就起调用作用
				- 比如，python需要 pip install autopep8
			- 使用：输入Neoformat命令,更多推荐查文档
		- autoformat
	- 静态检查；
		- w0rp/ale
			- 注意依赖,需要安装对应的库。插件本身就起调用作用
				- python需要 pip install pylint
			- 安装：Plug 'w0rp/ale'
			- 一开始会有很多提示，可以根据需要配置忽略项
			- 使用：自动就给你检查了
		- neomake

- 快速注释代码插件：
	- 安装：Plug 'tpope/vim-commentary'
	- 会根据不同语言使用不同注释
	- 常用命令：
		- gc 设置取消注释切换（后面来基本不怎么用）
		- gcc:注释
		- gcgc:取消注释

- git结合
	- tpope/Fugitive
		- 安装：Plug `tpope/vim-fugitive`
		- 作用：git的包装器,可以通过内置命令调用git
		- 不喜欢看后面的tmux开一个窗口用git
		- 使用：查文档，有具体命令
	- airblade/vim-gitgutter
		- 安装：Plug `airblade/vim-gitgutter`
		- 作用：会在每行左侧显示每行是新增的，删除的，等等
		- 使用：每行都会显示，不用主动调用
	- gv.vim
		- 注意：要安装了fugitive才行
		- 安装：Plug 'junegunn/gv.vim'
		- 作用：浏览代码提交变更,和git里面打 git log --ongline --graph --decorate 效果差不多基本相同
		- 使用：GV

# 4. 联合工具

-  Tmux
	- 功能：
		- 复用终端
		- 分屏
		- 托管进程
	- window上基本没法用，以后具体使用时看视频和文章吧
	- 文章位置:https://zhuanlan.zhihu.com/p/43687973：

- 嵌入开发工具
	- vsc超牛
		- 配置：
			```json
			// 按键快捷键配置
			{
				"key": "ctrl+s",
				"command": "cursorEnd",
				"when": "editorFocus && vim.mode == 'Insert'"
			},
			{
				"key": "ctrl+j",
				"command":"cursorLeft",
				"when": "editorFocus && vim.mode == 'Insert'"
			},
			{
				"key": "ctrl+k",
				"command": "cursorDown",
				"when": "editorFocus && vim.mode == 'Insert'"
			}
			,{
				"key": "ctrl+i",
				"command":"cursorUp",
				"when": "editorFocus && vim.mode == 'Insert'"
			},
			{
				"key": "ctrl+l",
				"command": "cursorRight",
				"when": "editorFocus && vim.mode == 'Insert'"
			}
			```
			```json
			// 设置配置
			"editor.lineNumbers": "relative",
			"vim.vimrc.path": "D:\\learn\\Microsoft VS code-workplace\\工作区\\vimrc",
			"vim.vimrc.enable": true,
			"vim.easymotion": true,
			"vim.foldfix": true,
			"vim.leader": "<space>",
			"vim.surround": true,
			"vim.easymotionDimBackground": false,
			"vim.easymotionMarkerBackgroundColor": "#FF4826",
			"vim.easymotionMarkerForegroundColorOneChar": "#FFFFFF",
			"vim.easymotionMarkerForegroundColorTwoChar": "#FFFFFF"
			"vim.hlsearch": true,
			"vim.incsearch": true,
			"vim.autoindent": true
			```
		```
		快速定位到行首（注：<leader> 表示上面设置的空格键）
		<leader> + <leader> j，定位光标以下的行首
		<leader> + <leader> k，定位光标以上的行首
		快速定位单词首
		<leader> + <leader> w，定位光标后面的单词首
		<leader> + <leader> b，定位光标前面的单词首
		快速定位单词尾
		<leader> + <leader> e，定位光标后面的单词尾
		<leader> + <leader> ge，定位光标前面的单词尾
		
		设置了相对行后
		往上跳转 n 行: n + k
		往下跳转 n 行: n + j

		surround-input：和插件一样
		ys添加 ds删除 cs改变
		```

- neovim
	- vim的一个分支
	- 改掉vim老旧的代码
	- 开发更活跃，更丰富的特性和扩展
	- 可以嵌入到很多GUI中
	- 基本上可以完全替代vim

- 开源配置：
	- SpaceVim/SpaceVim
	- PegasusWang/vim-config(视频作者配置)
	- 不建议新手直接用，越复杂成本越高。可以看一下SpaceVim，牛炸，用的时候就相当于黑盒