/* 
 * Copyright 2014 NIBSS
 * http://www.nibss-plc.com.ng
 * 
 */

(function ($) {
    App = {
        HTTP_POST: 'POST',
        HTTP_GET: 'GET',
        HTTP_PUT: 'PUT',
        HTTP_DELETE: 'DELETE',
        SEVERITY_SUCCESS: 'success',
        SEVERITY_INFO: 'info',
        SEVERITY_WARNING: 'warning',
        SEVERITY_ERROR: 'error',
        CONTENT_TYPE_FORM: 'application/x-www-form-urlencoded',
        CONTENT_TYPE_JSON: 'application/json',
        init: function () {

            $(document).ajaxStart(function () {
            });
            $(document).ajaxStop(function () {
            });
            $(document).ajaxError(function (event, jqxhr, settings, exception) {

                if (jqxhr.status) {
                    try {
                        if (jqxhr.responseText && jqxhr.responseText.length) {
                            var errorHtml = $(jqxhr.responseText).find('.errorWrapper');
                            App.util.showMessage(errorHtml.length ? errorHtml.html() : jqxhr.responseText, App.SEVERITY_ERROR);
                            return;
                        }
                    } catch (err) {
                    }
                    App.util.showMessage(exception + ': An Unexpected Error occurred while processing your request', App.SEVERITY_ERROR);
                }
            });
            App.applyTooltip($('[title!=""]'));
        },
        applyTooltip: function (container, options) {
            var defatltOptions = {
                position: {
                    my: 'bottom center',
                    at: 'top center',
                    viewport: $(window)
                },
                style: {classes: 'qtip-tipsy qtip-rounded', tip: {width: 10, height: 4}}
            };
            container.qtip($.extend(true, defatltOptions, options || {}));
        },
        resetPassword: function (contextPath) {
            if ($('#loginForm').length === 1) {
                var emailInput = $('#loginForm input#email');
                if (emailInput.val() === "") {
                    App.util.showMessage("Email Field is required", App.SEVERITY_WARNING);
                    return;
                }
                var options = {
                    title: "Forgot Password",
                    message: "Are you sure you want to perform a Password Reset.<br/><b> Email : " + emailInput.val() + "</b>",
                    buttons: {
                        ok: {
                            callback: function () {
                                $('#loginButton').attr("disabled", true);
                                App.ajax.doAction({
                                    form: $('#loginForm'),
                                    ajax: {
                                        url: contextPath + '/core/profile/reset-password',
                                        contentType: App.CONTENT_TYPE_FORM
                                    },
                                    onsuccess: function (response) {
                                        if (response && response.valid) {
                                            window.location.replace(contextPath + '/main.jspx');
                                        }
                                    },
                                    oncomplete: function () {
                                        $('#loginButton').removeAttr("disabled");
                                    }
                                });
                            }
                        }
                    }
                };
                new App.ui.modalBox(options).show();
            }
        },
        login: function (contextPath) {
            if ($('#loginForm').length === 1) {
                $('#loginButton').attr("disabled", true);
                App.ajax.doAction({
                    form: $('#loginForm'),
                    ajax: {
                        url: contextPath + '/core/login',
                        contentType: App.CONTENT_TYPE_FORM
                    },
                    onsuccess: function (response) {
                        if (response && response.valid) {
                            window.location.replace(contextPath + '/main.jspx');
                        }
                    },
                    oncomplete: function () {
                        $('#loginButton').removeAttr("disabled");
                    }
                });
            }
        },
        logout: function (contextPath) {/**
         App.ajax.doAction({
         ajax: {
         url: contextPath + '/core/login/logout'
         },
         onsuccess: function (response) {
         if (response) {
         if (response.valid)
         window.location.replace(contextPath + '/main.jspx');
         else
         window.location.replace(contextPath + '/logout.jspx');
         }
         },
         onfail: function () {
         window.location.replace(contextPath + '/logout.jspx');
         }
         });**/
            window.location.replace(contextPath + '/logout.jspx');
        }
    };
    App.ui = {
        datatable: function (options) {
            var SELECTED_CLASS = "active selected";
            options = $.extend(true, {
                serverSide: true,
                searching: false,
                processing: true,
                selection: "multiple",
                export: "datatable/export",
                pagingType: "full_numbers",
                lengthMenu: [20, 50, 100, 200, 500],
                order: [[0, 'desc']],
                ajax: {
                    url: "datatable/load",
                    type: App.HTTP_POST,
                    contentType: App.CONTENT_TYPE_JSON + "; charset=utf-8",
                    data: function (data) {
                        return self.requestData(data);
                    }
                },
                headerCallback: function (thead, data, start, end, display) {
                    initColumnHeader(thead);
                },
                drawCallback: function () {
                    fireStateChangeEvent();
                },
                language: {
                    processing: "",
                    lengthMenu: "Records Per Page: _MENU_"
                }
            }, options || {});
            var self = this;
            var jqId = '#' + options.id;
            var init = function () {
                //$(jqId).off('draw.dt').on('draw.dt', function () {initState();} );
                self.stateChangeListeners = {};
                initColumns();
                self.dataTables = $(jqId).DataTable(options);
                initToolbar();
            };
            var fireStateChangeEvent = function () {
                $.each(self.stateChangeListeners, function (key, value) {
                    if ($.isFunction(value)) {
                        value.call();
                    }
                });
            };
            var initToolbar = function () {
                $(jqId).appendTo($('<div>').css(options.scrollable ? {overflow: 'auto'} : {}).addClass('datatable-container').appendTo($(jqId).parent('div:first')));
                var toolbarLeft = $(jqId + '_wrapper div:first div:first').addClass('btn-toolbar').css({'margin-left': '0px'});
                var toolbarRight = $(jqId + '_wrapper div:first div:last').addClass('btn-toolbar').css({'text-align': 'right'});
                toolbarLeft.prepend($(jqId + '_toolbarLeft').children());
                toolbarRight.append($(jqId + '_toolbarRight').children());
                self.stateChangeListeners['toolbar'] = function () {
                    var state = !self.hasSelection();
                    toolbarLeft.find('.row-selection-enabled').prop('disabled', state);
                    toolbarRight.find('.row-selection-enabled').prop('disabled', state);
                };
            };
            var initColumns = function () {
                if ($.isArray(options.columns)) {
                    $.each(options.columns, function (index, column) {
                        if (!column || !(column.actions || column.selector || column.numbering || column.dataType)) {
                            return;
                        }
                        column.onrender = column.render;
                        column.oncreatedCell = column.createdCell;
                        if (column.dataType === 'date' || column.dataType === 'timestamp') {
                            renderDateColumn(column);
                        } else if (column.dataType === 'currency' || column.dataType === 'format-number') {
                            renderNumberFormatColumn(column);
                        } else {
                            sanitizeColumn(column);
                            if (column.actions) {
                                renderActionsColumn(column);
                            } else if (column.selector) {
                                renderSelectorColumn(column);
                            } else if (column.numbering) {
                                renderNumberingColumn(column);
                            }
                        }
                    });
                }
            };
            var initColumnHeader = function (thead) {
                if ($.isArray(options.columns)) {
                    $.each(options.columns, function (index, column) {
                        if (!column || (column.title && $.trim(column.title) !== '') || !(column.actions || column.selector || column.numbering)) {
                            return;
                        }
                        if (column.actions) {
                        }
                        else if (column.selector) {
                            if (options.selection === 'multiple') {
                                //$(thead).find('th').eq(index);
                                if (!$(thead).find('th').eq(index).hasClass("selector")) {
                                    var jqCell = $(thead).find('th').eq(index).addClass("selector").append($('<input>').attr('type', 'checkbox')).off('ifChecked ifUnchecked')
                                            .on('ifChecked', function (event) {
                                                self.selectAllRows();
                                            })
                                            .on('ifUnchecked', function (event) {
                                                self.deselectAllRows();
                                            });
                                    App.ui.iCheck(jqCell, {skin: 'orange'});
                                    self.stateChangeListeners['selectorTH_' + index] = function () {
                                        jqCell.iCheck(self.isEmpty() ? 'disable' : 'enable');
                                    };
                                }
                            }
                        } else if (column.numbering) {
                            $(thead).find('th').eq(index).html('<span class="label">#</span>');
                        }
                    });
                }
            };
            var renderDateColumn = function (column) {
                column.render = function (data, type, rowData, meta) {
                    if (type !== 'display')
                        return data;
                    if ($.isFunction(column.onrender))
                        column.onrender.call(this, data, type, rowData, meta, html);
                    return  App.util.timestampToDate(data);
                };
            };
            var renderNumberFormatColumn = function (column) {
                column.render = function (data, type, rowData, meta) {
                    if (type !== 'display')
                        return data;
                    if ($.isFunction(column.onrender))
                        column.onrender.call(this, data, type, rowData, meta, html);
                    return  App.util.formatNumber(data);
                };
            };
            var renderSelectorColumn = function (column) {
                var multipleSelection = options.selection === 'multiple';
                column.render = function (data, type, rowData, meta) {
                    if (type !== 'display')
                        return data;
                    var html = $('<input>').attr('type', multipleSelection ? 'checkbox' : 'radio').prop("disabled", !data);
                    if ($.isFunction(column.onrender))
                        column.onrender.call(this, data, type, rowData, meta, html);
                    return $('<div>').append(html).html();
                };
                column.createdCell = function (cell, cellData, rowData, rowIndex, colIndex) {
                    var jqCell = $(cell).addClass("selector").find('input').off('ifChecked ifUnchecked')
                            .on('ifChecked', function (event) {
                                self.selectRow($(cell).parent('tr'));
                            })
                            .on('ifUnchecked', function (event) {
                                self.deselectRow($(cell).parent('tr'));
                            });
                    App.ui.iCheck(jqCell, {skin: 'blue', handle: multipleSelection ? 'checkbox' : 'radio'});
                    if ($.isFunction(column.oncreatedCell)) {
                        column.oncreatedCell.call(this, cell, cellData, rowData, rowIndex, colIndex);
                    }
                };
            };
            var renderNumberingColumn = function (column) {
                column.render = function (data, type, rowData, meta) {
                    if (type !== 'display')
                        return data;
                    var html = $('<span class="label">').text(meta.row + 1);
                    if ($.isFunction(column.onrender))
                        column.onrender.call(this, data, type, rowData, meta, html);
                    return $('<div>').append(html).html();
                };
                column.createdCell = function (cell, cellData, rowData, rowIndex, colIndex) {
                    if ($.isFunction(column.oncreatedCell))
                        column.oncreatedCell.call(this, cell, cellData, rowData, rowIndex, colIndex);
                };
            };
            var renderActionsColumn = function (column) {
                column.render = function (data, type, rowData, meta) {
                    if (type !== 'display')
                        return data;
                    var toolbar = $('<div>').addClass('btn-toolbar'); //<div class="btn-toolbar">
                    $.each(column.actions, function (key, action) {
                        var rendered = action.rendered;
                        if ($.isFunction(rendered)) {
                            rendered = rendered.call(this, data, rowData, meta);
                        }
                        if (typeof rendered !== 'undefined' && !rendered) {
                            return true;
                        }
                        var disabled = action.disabled;
                        if ($.isFunction(disabled)) {
                            disabled = disabled.call(this, data, rowData, meta);
                        }

                        var btn = $('<a class="btn" data-toggle="tooltip" href="javascript:void(0)" style="overflow: hidden; position: relative;"></a>');
                        if (action.title)
                            btn.attr({title: action.title});
                        if (action.className)
                            btn.addClass(action.className);
                        if (!action.label && !action.icon) {//the lack of an explicit label and icon means we'll assume the key is good enough
                            action.label = key;
                        }
                        if (action.label)
                            btn.html(' ' + action.label);
                        if (action.icon)
                            btn.prepend($('<i>').addClass(action.icon));
                        if (disabled)
                            btn.attr('disabled', true).addClass('disabled');
                        toolbar.append(btn.addClass('datatable-action-' + key));
                    });
                    if ($.isFunction(column.onrender))
                        column.onrender.call(this, data, type, rowData, meta, toolbar);
                    return $('<div>').append(toolbar).html();
                };
                column.createdCell = function (cell, cellData, rowData, rowIndex, colIndex) {
                    $.each(column.actions, function (key, action) {
                        var cellBtn = $(cell).find('.datatable-action-' + key);
                        if (cellBtn && action && $.isFunction(action.callback)) {
                            cellBtn.click(function () {
                                action.callback.call(this, cellData, rowData, rowIndex, colIndex);
                            });
                        }
                    });
                    App.applyTooltip($(cell).find('[title!=""]'));
                    if ($.isFunction(column.oncreatedCell))
                        column.oncreatedCell.call(this, cell, cellData, rowData, rowIndex, colIndex);
                };
            };
            var sanitizeColumn = function (column) {
                if (!column.searchable)
                    column.searchable = false;
                if (!column.orderable)
                    column.orderable = false;
                if (column.actions) {
                    var defaultActions = {
                        view: {
                            icon: "fa fa-eye",
                            className: " btn-flat btn-sm btn-info"
                        },
                        edit: {
                            icon: "fa fa-pencil",
                            className: " btn-flat btn-sm btn-success"
                        },
                        delete: {
                            icon: "fa fa-trash",
                            className: " btn-flat btn-sm btn-warning"
                        }
                    };
                    $.each(column.actions, function (key, action) {
                        if ($.isFunction(action)) {//callback is shorthand
                            action = column.actions[key] = {
                                callback: action
                            };
                        }
                        if ($.type(action) !== "object") {
                            throw new Error("button with key " + key + " must be an object");
                        }

                        if (!action.title) {// the lack of an explicit title means we'll assume the key is good enough
                            action.title = key;
                        }
                        if (defaultActions[key]) {
                            column.actions[key] = $.extend(true, defaultActions[key], column.actions[key]);
                        }
                    });
                    //column.actions = $.extend(true, defaultActions, column.actions);
                }
            };
            this.requestData = function (data) {
                data = data ? data : {};
                var jqForm = $(jqId + '_container ' + jqId + "_tableFilterForm");
                data['filters'] = $.extend(true, data['filters'] || {}, jqForm.length ? App.util.serializeFormToJson(jqForm, false) : {});
                return JSON.stringify(data);
            };
            this.reload = function (reset) {
                var dataTable = $(jqId).DataTable();
                dataTable.clear();
                dataTable.draw(reset);
            };
            this.draw = function (reset) {
                var dataTable = $(jqId).DataTable();
                dataTable.clear();
                dataTable.draw(reset);
            };
            this.selectRow = function (rowTR) {
                $(rowTR).addClass(SELECTED_CLASS);
                fireStateChangeEvent();
            };
            this.deselectRow = function (rowTR) {
                $(rowTR).removeClass(SELECTED_CLASS);
                fireStateChangeEvent();
            };
            this.selectAllRows = function () {
                $(jqId).DataTable().rows().every(function () {
                    //$(this.node()).addClass(SELECTED_CLASS).find('td.selector input[type="checkbox"]:enabled').iCheck('check');
                    var nodes = $(this.node()).find('td.selector input[type="checkbox"]:enabled');
                    if (nodes.length) {
                        nodes.iCheck('check');
                        $(this.node()).addClass(SELECTED_CLASS);
                    }
                });
                fireStateChangeEvent();
            };
            this.deselectAllRows = function () {
                $(jqId).DataTable().rows().every(function () {
                    var nodes = $(this.node()).find('td.selector input[type="checkbox"]:enabled');
                    if (nodes.length) {
                        nodes.iCheck('uncheck');
                        $(this.node()).removeClass(SELECTED_CLASS);
                    }
                });
                fireStateChangeEvent();
            };
            this.isEmpty = function () {
                return  $(jqId).DataTable().rows().length === 0;
            };
            this.hasSelection = function () {
                return $(jqId + " tbody tr").not(":has(td.dataTables_empty)").hasClass(SELECTED_CLASS);
            };
            this.getSelection = function (selectionData) {
                var rowData = [];
                var intData = selectionData ? !isNaN(parseInt(selectionData)) : false;
                $(jqId).DataTable().rows().every(function () {
                    if ($(this.node()).hasClass(SELECTED_CLASS)) {
                        var data = this.data();
                        if (intData) {
                            var _selectionData = parseInt(selectionData);
                            if ($.isArray(data) && data.length > _selectionData && _selectionData > -1) {
                                rowData.push(data[_selectionData]);
                            }
                        } else if (typeof selectionData === 'string') {
                            var value = App.util.evalJSONValue(selectionData, data);
                            if (value)
                                rowData.push(value);
                        } else {
                            rowData.push(data);
                        }
                    }
                });
                //$(jqId + " tbody tr.active.selector").not(":has(td.dataTables_empty)");
                return rowData;
            };
            this.filterToggled = function () {
                window.setTimeout(function () {
                    var collapsed = $(jqId + "_container").hasClass('collapsed-box');
                    App.ajax.doAction({ajax: {
                            uri: 'datatable/filter-collapsed/' + collapsed,
                            type: App.HTTP_PUT,
                            data: 'state=' + collapsed
                        }});
                }
                , 100);
            };
            this.export = function () {
                App.ajax.fileDownload(options.export);
            };
            window[options.id] = this;
            $(function () {
                init();
            });
            return this;
        },
        modalBox: function (options) {
            var isBootbox = !options.id || $('#' + options.id + '.modal').length === 0;
            options = $.extend(true, {
                buttons: {
                    ok: {
                        label: "OK",
                        className: "btn-flat btn-success"
                    },
                    cancel: {
                        label: "Cancel",
                        className: "btn-flat btn-default",
                        callback: function () {

                        }
                    }
                },
                ajax: {
                    contentType: 'text/html',
                    success: function (data, status, jqXHR) {
                        $.unblockUI();
                        if (isBootbox) {
                            options.message = data;
                            if (options.message.length > 0)
                                bootbox.dialog(options);
                        } else {
                            $(jqId + ' .modal-body').html(data);
                            $(jqId).modal({show: true});
                        }
                        if ($.isFunction(options.ajax.onsuccess)) {
                            options.ajax.onsuccess.call(this, data);
                        }
                    },
                    error: function (jqXHR, status, error) {
                        $.unblockUI();
                        if ($.isFunction(options.ajax.onerror)) {
                            options.ajax.onerror.call(this, error);
                        }
                    },
                    complete: function () {
                        // $.unblockUI();
                    }
                }
            }, options || {});
            var self = this;
            var jqId = options.id ? '#' + options.id : '';
            var init = function () {
            };
            this.show = function (_options) {
                _options = $.extend(true, $.extend(true, {}, options || {}), _options || {});
                if (_options.ajax && _options.ajax.url) {
                    $.blockUI({message: null, css: {border: '1px solid #ccc'}});
                    setTimeout(function () {
                        $.ajax(_options.ajax);
                    }, 200);
                } else {
                    if (isBootbox) {
                        if (_options.message.length > 0)
                            bootbox.dialog(_options);
                    } else {
                        $(jqId).modal({show: true});
                    }
                }
            };
            this.hide = function () {
                if (isBootbox) {
                    bootbox.hideAll();
                } else {
                    $(jqId).modal('hide');
                }
            };
            if (options.id)
                window[options.id] = this;
            $(function () {
                init();
            });
            return this;
        },
        confirmDialogBox: function (options, callback) {
            options = typeof options === 'string' ? {title: options} : options;
            options = $.extend(true, {
                message: "Are you sure you want to perform this action?",
                buttons: {
                    ok: {
                        callback: function () {
                            if ($.isFunction(callback))
                                callback.call(this);
                        }
                    }
                }
            }, options);
            return new App.ui.modalBox(options);
        },
        iCheck: function (jq, options) {
            jq = $.type(options) === 'string' ? $('#' + jq) : jq;
            if (jq instanceof $) {
                var skin = (options && options.skin) ? '-' + options.skin : '-blue';
                jq.iCheck($.extend(true, {
                    checkboxClass: 'icheckbox_square' + skin,
                    radioClass: 'iradio_square' + skin
                },
                options || {}));
            }
        },
        selectDropdown: function (options) {
            var self = this;
            var ajaxOptions = {
                ajax: {
                    beforeSend: function (xhr) {
                        var _options = self._options;
                        if (_options.ajaxIndicator instanceof $) {
                            _options.ajaxIndicator.show();
                        }
                    }
                },
                onsuccess: function (response) {
                    var _options = self._options;
                    if (response.valid) {
                        var data = response.data ? response.data : $.isArray(response) ? response : [];
                        if (!_options.appendOptions) {
                            data = $.merge($.merge([], self.initOptionsData), data);
                            $(jdId).children('option').remove();
                        }
                        $.each(data, function () {
                            if (this.value !== null && $.type(this.value) !== 'undefined' && this.label !== null && $.type(this.label) !== 'undefined') {
                                var option = $('<option>').attr({value: this.value}).text(this.label);
                                $(jdId).append(option);
                            }
                        });
                        selectState(_options);
                    }
                    if ($.isFunction(_options.onsuccess)) {
                        _options.onsuccess.call(this, response);
                    }
                },
                onfail: function (response) {
                    var _options = self._options;
                    if ($.isFunction(_options.onfail)) {
                        _options.onfail.call(this, response);
                    }
                },
                oncomplete: function (response) {
                    var _options = self._options;
                    if (_options.ajaxIndicator instanceof $) {
                        _options.ajaxIndicator.hide();
                    }
                    if ($.isFunction(_options.oncomplete)) {
                        _options.oncomplete.call(this, response);
                    }
                }
            };
            options = $.extend(true, {
                ajax: {
                },
                noSelectOption: true,
                multiple: true
            }, options || {});
            var jdId = options.id instanceof $ ? options.id : '#' + options.id;
            var init = function () {
                self.initOptionsData = [];
                $.each($(jdId).children("option"), function () {
                    var option = $(this);
                    self.initOptionsData.push({label: option.text(), value: option.attr("value")});
                });
                if ($.isFunction(options.onchange)) {
                    $(jdId).change(function () {
                        options.onchange.call(this);
                    });
                }
                if (!options.deferred)
                    self.update(options);
            };
            this.update = function (_options) {
                _options = $.extend(true, $.extend(true, {}, options || {}), _options || {});
                if (_options.ajax.url) {
                    if (_options.ajaxIndicator) {
                        _options.ajaxIndicator = $.type(options.ajaxIndicator) === 'string' ? $('#' + options.ajaxIndicator) : options.ajaxIndicator;
                    }
                    self._options = _options;
                    App.ajax.load($.extend(true, $.extend(true, {}, ajaxOptions), {ajax: _options.ajax}));
                } else {
                    selectState(_options);
                }
            };
            var selectState = function (options) {
                if (options.selectedValue)
                    $(jdId + ' option[value="' + options.selectedValue + '"]').prop('selected', true);
                if (options.noSelectOption) {
                    var val = $(jdId).val();
                    if (val && val !== '') {
                        $(jdId + ' option[value=""]').remove();
                    }
                }
            };
            this.getSelectedValue = function () {
            };
            window[options.id] = this;
            $(function () {
                init();
            });
        },
        treeView: function (options) {
            var PREFIX_ID = "treeNode_";
            var self = this;
            var ajaxOptions = {
                ajax: {
                    beforeSend: function (xhr) {
                        var _options = self._options;
                        if (_options.ajaxIndicator instanceof $) {
                            _options.ajaxIndicator.show();
                        }
                    }
                },
                onsuccess: function () {
                    var _options = self._options;
                    if ($.isFunction(_options.onsuccess)) {
                        _options.onsuccess.call(this, response);
                    }
                },
                onfail: function (response) {
                    var _options = self._options;
                    if ($.isFunction(_options.onfail)) {
                        _options.onfail.call(this, response);
                    }
                },
                oncomplete: function () {
                    var _options = self._options;
                    if (_options.ajaxIndicator instanceof $) {
                        _options.ajaxIndicator.hide();
                    }
                }
            };
            //selectionIcon
            options = $.extend(true, {
                core: {
                    data: function (node, callback) {
                        var _options = self._options;
                        if (_options && _options.ajax && _options.ajax.url) {
                            var _ajaxOptions = {ajax: _options.ajax, form: _options.form, onsuccess: function (response) {
                                    if ($.isFunction(callback)) {
                                        callback.call(this, processData(response.data));
                                    }
                                    if ($.isFunction(_options.onsuccess)) {
                                        options.onsuccess.call(this, response);
                                    }
                                }, onfail: function () {
                                    if ($.isFunction(callback)) {
                                        callback.call(this, []);
                                    }
                                }};
                            App.ajax.load($.extend(true, $.extend(true, {}, ajaxOptions), _ajaxOptions));
                        }
                    }
                }
            }, options || {});
            var jdId = options.id instanceof $ ? options.id : '#' + options.id;
            var init = function () {
                if (!options.deferred) {
                    self.update(options);
                }

            };
            var processData = function (data, parentIndex) {
                if (!$.isArray(data)) {
                    return data;
                }

                options = self._options;
                $.each(data, function (index, row) {
                    if ($.type(row) !== 'object') {
                        return;
                    }
                    if ((row.value || row.id) && options.selectedValues && $.isArray(options.selectedValues)) {
                        $.each(options.selectedValues, function (index, value) {
                            if (!row.state)
                                row.state = {};
                            row.state.selected = value == (row.value || row.id);
                            if (options.selectionIcon)
                                row.icon = row.state.selected ? 'fa fa-check-circle' : 'fa fa-dot-circle-o';
                            return !row.state.selected;
                        });
                    }
                    row.id = PREFIX_ID + (row.id ? row.id : (($.typeof(parentIndex) === 'number' ? parentIndex + "_" + index : index)));
                    if (row.parent && row.parent !== '#')
                        row.parent = PREFIX_ID + (row.parent ? row.parent : ($.typeof(parentIndex) === 'number' ? parentIndex + "_" + index : index));
                    if (row.value) {
                        row.li_attr = $.extend(true, {value: row.value}, row.li_attr || {});
                    }
                    if (row.children) {
                        processData(row.children, index);
                    }
                });
                return data;
            };
            this.update = function (_options) {
                _options = $.extend(true, $.extend(true, {}, options), _options || {});
                if (_options.selectedInputField instanceof $ && !_options.selectedValues) {
                    var val = _options.selectedInputField.val();
                    if (val)
                        _options.selectedValues = val.split(',');
                }
                self._options = _options;
                $(jdId).jstree('destroy');
                $(jdId).jstree(self._options);
                addListener();
            };
            var addListener = function () {
                $(jdId).off('changed.jstree.selector').on('changed.jstree.selector', function (e, data) {
                    if (self._options && self._options.checkbox &&
                            typeof self._options.checkbox.three_state !== 'undefined'
                            && !self._options.checkbox.three_state
                            && data.instance.is_selected(data.node)) {
                        data.instance.select_node(data.node.parents, true);
                    }
                    var _options = self._options;
                    if (_options.selectedInputField instanceof $) {
                        var selected = [];
                        $.each(data.selected, function (index, nodeData) {
                            var id = data.instance.get_node(data.selected[index]).id;
                            selected.push(id.substring(id.indexOf("_") + 1));
                        });
                        _options.selectedInputField.val(selected.join(','));
                    }
                });
            };
            this.getSelectedValues = function () {
                var selected = [];
                $.each($(jdId).jqTree(true).get_selected(true), function (index, node) {
                    selected.push($(node).attr('value'));
                });
                return selected;
            };
            this.getSelectedIds = function () {
                var selected = [];
                $.each($(jdId).jqTree(true).get_selected(), function (index, id) {
                    selected.push(id.substring(id.indexOf(PREFIX_ID)));
                });
                return selected;
            };
            window[options.id] = this;
            $(function () {
                init();
            });
        }
    };
    App.ajax = {
        load: function (options) {
            if (!options.ajax || !options.ajax.url) {
                return;
            }
            if (typeof (options.ajax.contentType) !== 'undefined' && options.ajax.contentType !== null && options.ajax.contentType !== App.CONTENT_TYPE_JSON) {
                options.ajax.data = (options.form ? options.form.serialize() : "") + ($.isPlainObject(options.ajax.data) ? "&" + $.param(options.ajax.data) : (options.ajax.data || ""));
            }
            else {
                //options.ajax.data = JSON.stringify($.extend(true, (options.form ? App.util.serializeFormToJson(options.form, true) : {}), options.ajax.data || {}));
                options.ajax.data = $.extend(true, (options.form ? App.util.serializeFormToJson(options.form, true) : {}), options.ajax.data || {});
                options.ajax.data = options.form ? JSON.stringify(options.ajax.data) : options.ajax.data;
            }
            var _options = $.extend(true, {
                type: options.form ? App.HTTP_POST : App.HTTP_GET,
                dataType: "json",
                contentType: App.CONTENT_TYPE_JSON + "; charset=utf-8",
                cache: false,
                success: function (response) {
                    if (response && response.messages) {
                        App.util.hideMessages(options.form);
                        App.util.showMessages(response.messages, options.form);
                    }
                    if ($.isFunction(options.onsuccess)) {
                        options.onsuccess.call(this, response);
                    }
                },
                fail: function (response) {
                    if ($.isFunction(options.onfail)) {
                        options.onfail.call(this, response);
                    }
                },
                complete: function (response) {
                    if ($.isFunction(options.oncomplete)) {
                        options.oncomplete.call(this, response);
                    }
                }}, options.ajax || {});
            $.ajax(_options);
        },
        doAction: function (options) {
            return App.ajax.load(options);
        },
        fileDownload: function (options) {
            options = $.type(options) === 'string' ? {url: options} : options;
            if (!options.url || !$.fileDownload)
                return;
            options = $.extend(true, {
                httpMethod: "POST",
                message: "Download in Progress...",
                successMessage: "File Download was Successful",
                failMessage: "There was a problem generating the file, please try again."
            }, options || {});
            App.util.blockUI(options.message);
            $.fileDownload(options.url, options)
                    .done(function () {
                        $.unblockUI();
                        if (options.successMessage)
                            App.util.showMessage(options.successMessage, App.SEVERITY_SUCCESS);
                    })
                    .fail(function () {
                        $.unblockUI();
                        if (options.failMessage)
                            App.util.showMessage(options.failMessage, App.SEVERITY_WARNING);
                    });
        }
    };
    App.util = {
        blockUI: function (msg) {
            $.blockUI({message: msg || "Processing", css: {'background-color': '#333333', color: '#ffffff', 'font-size': '15px', padding: '10px', opacity: '0.8'}});
        },
        showMessage: function (msg, severity) {
            if (!msg)
                return;
            App.util.showMessages({detail: msg, severity: severity ? severity : App.SEVERITY_INFO});
        },
        showMessages: function (messages, fieldContainer) {
            if (!messages) {
                return;
            }
            $.each($.isArray(messages) ? messages : [messages], function (index, msg) {
                if ($.type(msg) === "string") {
                    msg = {detail: msg, severity: 'info'};
                }
                if ($.type(msg) !== "object" || (!msg.detail && !msg.summary)) {
                    return;
                }
                var field = (msg.fieldId && fieldContainer) ? fieldContainer.find(":input[name='" + msg.fieldId + "']") : null;
                msg.severity = $.inArray(msg.severity, [App.SEVERITY_INFO, App.SEVERITY_ERROR, App.SEVERITY_WARNING, App.SEVERITY_SUCCESS]) === -1 ? App.SEVERITY_INFO : msg.severity;
                var iconType = 'fa fa-' + (msg.severity === App.SEVERITY_ERROR ? 'exclamation' : (msg.severity === App.SEVERITY_SUCCESS ? 'check' : msg.severity));
                if (field && field.length) {
                    var inlineMsg = field.next('.inline-alert');
                    if (!inlineMsg || inlineMsg.length === 0) {
                        inlineMsg = $('<label class="inline-alert"></label>');
                        field.parents('.form-group:first').append(inlineMsg);
                    }

                    inlineMsg.text(' ' + msg.summary).show();
                    inlineMsg.prepend($('<i>').addClass(iconType));
                    field.parents('.form-group:first').addClass('has-' + msg.severity);
                } else {
                    $.notify({title: (msg.detail ? (msg.summary ? msg.summary + "<br/>" : '') : ''),
                        message: msg.detail ? msg.detail : msg.summary,
                        icon: iconType,
                        type: msg.severity === App.SEVERITY_ERROR ? 'danger' : msg.severity
                    }, {
                        delay: 10000,
                        mouse_over: 'pause',
                        z_index: 1531,
                        spacing: 15, newest_on_top: true
                    });
                }
            });
            if (fieldContainer && fieldContainer.length)
                App.applyTooltip(fieldContainer.find('[title!=""]'));
        },
        hideMessages: function (fieldContainer) {
            //$.notifyClose();
            if (fieldContainer && fieldContainer instanceof $ && fieldContainer.length) {
                //fieldContainer.find('.form-group').removeClass("has-error has-warning has-success");
                //fieldContainer.find('.form-group .inline-alert').text("").hide();
                fieldContainer.find('input').each(function (index) {
                    var input = $(this);
                    input.parents('.form-group:first').removeClass("has-error has-warning has-success");
                    input.next('.inline-alert').hide();
                });
            }
        },
        serializeFormToJson: function (jqForm, deep) {
            var o = {};
            var a = jqForm.serializeArray();
            var serializeValue = function (layer, path, value) {
                var i = 0;
                path = deep ? path.split('.') : [path];
                for (; i < path.length; i++) {
                    if (value !== null && i + 1 === path.length) {
                        if (layer[path[i]] !== undefined) {
                            if (!layer[path[i]].push) {
                                layer[path[i]] = [layer[path[i]]];
                            }
                            layer[path[i]].push(value);
                        } else
                            layer[path[i]] = value;
                    } else
                        layer = layer[path[i]] = {};
                }
                return layer;
            };
            $.each(a, function () {
                serializeValue(o, this.name, this.value || '');
            });
            return o;
        },
        exec: function (value, _this, args) {
            _this = _this ? _this : this;
            args = $.isArray(args) ? args : [args];
            if (typeof value === 'string') {
                return (new Function(value)).apply(_this, args);
                // return eval(value);
            } else if ($.isFunction(value)) {
                return value.apply(_this, args);
            }
        },
        evalJSONValue: function (pathField, json) {
            try {
                $.parseJSON(json);
            } catch (err) {
                return null;
            }
            var root = json;
            $.each(pathField.split('.'), function (index, path) {
                if (!root)
                    return false;
                root = root[path];
            });
            return root;
        },
//         isDigit:function(value){
//  if((parseFloat(value) == parseInt(value)) && !isNaN(value)){
//      return true;
//  } else {
//      return false;
//  }
//}
        isEnterKey: function (event) {
            var keycode = window.event ? window.event.keyCode : (event ? event.keyCode ? event.keyCode : event.which : null);
            return (keycode === 13 || keycode === '13');
        },
        timestampToDate: function (timestamp) {
            if (!timestamp)
                return "";
            var now = new Date(timestamp);
            return now.toDateString() + " " + now.toLocaleTimeString();
            //return new Date(timestamp).toUTCString();
        },
        formatNumber: function (value) {
            if (!numeral)
                return value;
            numeral.defaultFormat('0,0.00');
            return numeral(value).format();
        },
        filterDigitOnkeydown: function (event, inputField, isDecimal, isGrouping) {
            if (37 === event.keyCode || event.keyCode === 39 || event.keyCode === 46 || event.keyCode === 8) {
                return true;
            }
            else if (event.keyCode === 190) {
                if (isDecimal) {
                    var value = inputField.value;
                    if (value === "")
                        return false;
                    var dot = value.indexOf(".");
                    return (dot <= 0);
                }
                else
                    return false;
            }
            else if (event.keyCode === 188) {
                if (isDecimal) {
                    var value = inputField.value;
                    if (value === "")
                        return false;
                    var dot = value.indexOf(".");
                    return (dot <= 0);
                }
                else
                    return false;
                if (isGrouping && value !== '') {
                    pos = inputField === null ? -1 : doGetCaretPosition(inputField);
                    dot = value.indexOf(".");
                    return (dot < 0 || (pos > 0 && pos < dot));
                }
            }

            return ((event.keyCode >= 96 && event.keyCode <= 105) || (event.keyCode >= 48 && event.keyCode <= 57));
        },
        enforceDigits: function (inputField, isDecimal) {
            var value = inputField.value;
            var x;
            var dot = value.indexOf(".");
            var ind;

            for (x = 0; value.length > x; x++) {

                ind = value.substring(x, x + 1);
                if ("0" > ind || ind > "9") {
                    if (isDecimal) {
                        if (dot > 0) {
                            if (x === dot)
                                continue;
                        }
                    }
                    value = value.substring(0, x);
                    inputField.value = value;
                    break;
                }
            }
        }
    };
})(jQuery);
