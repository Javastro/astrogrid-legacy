/*
            'contents', 'Query<img alt="Query" src="Open.png" style="float:left"/>',
            'contentsHover', 'Query<img alt="Query" src="Open.png" style="float:left"/>',
*/
/*
domMenu_data.setItem(
    'agMain_menu',
    new domMenu_Hash(
        1, new domMenu_Hash(
            'contents', '&lt;Query&gt;',
            'contentsHover', '&gt;Query&lt;',
            'backgroundImage', 'Open.png',
            'uri', '',
            'target', '_self',
            'statusText', 'Query...',
            1, new domMenu_Hash(
                'contents', 'Load...',
                'uri', 'load-query.html',
                'target', '_blank',
                'statusText', 'Load a Query'),
            2, new domMenu_Hash(
                'contents', 'Save...',
                'uri', 'save-query.html',
                'target', '_blank',
                'statusText', 'Save the Query')),
        2, new domMenu_Hash(
            'contents', 'Help',
            'contentsHover', 'Help',
            'uri', '',
            'target', '_self',
            'statusText', 'Help...',
            1, new domMenu_Hash(
                'contents', 'About...',
                'uri', 'help-about.html',
                'target', '_blank',
                'statusText', 'About')
            )));
*/

domMenu_settings.setItem(
    'agMain_menu',
    new domMenu_Hash(
        'menuBarWidth', '0%',
        'menuBarClass', 'agMain_menu_menuBar',
        'menuElementClass', 'agMain_menu_menuElement',
        'menuElementHoverClass', 'agMain_menu_menuElementHover',
        'menuElementActiveClass', 'agMain_menu_menuElementHover',
        'subMenuBarClass', 'agMain_menu_subMenuBar',
        'subMenuElementClass', 'agMain_menu_subMenuElement',
        'subMenuElementHoverClass', 'agMain_menu_subMenuElementHover',
        'subMenuElementActiveClass', 'agMain_menu_subMenuElementHover',
        'subMenuMinWidth', 'auto',
        'horizontalSubMenuOffsetX', -5,
        'horizontalSubMenuOffsetY', 3,
        'distributeSpace', false,
        'openMouseoverMenuDelay', -1,
        'openMousedownMenuDelay', 0,
        'closeClickMenuDelay', 0,
        'closeMouseoutMenuDelay', -1
));

  domMenu_data.setItem(
      'agMain_menu',
      new domMenu_Hash(
      1, new domMenu_Hash(
        'contents', 'Query',
        'contentsHover', 'Query',
        'uri', '',
        'target', '_self',
        'statusText', 'Query...',
    1, new domMenu_Hash(
        'contents', 'Load',
        'contentsHover', 'Load',
        'icon', 'Open.gif',
        'uri', '',
        'target', '_self',
        'statusText', 'Load a query')
  ,2, new domMenu_Hash(
        'contents', 'Save',
        'contentsHover', 'Save',
        'icon', 'Save.gif',
        'uri', '',
        'target', '_self',
        'statusText', 'Save a query')
  )
  ,2, new domMenu_Hash(
        'contents', 'Help',
        'contentsHover', 'Help',
        'icon', 'Help.gif',
        'uri', '',
        'target', '_self',
        'statusText', 'Help...',
    1, new domMenu_Hash(
        'contents', 'About',
        'contentsHover', 'About',
        'icon', 'About.gif',
        'uri', '',
        'target', '_self',
        'statusText', 'About Portal')
  )
  ));
