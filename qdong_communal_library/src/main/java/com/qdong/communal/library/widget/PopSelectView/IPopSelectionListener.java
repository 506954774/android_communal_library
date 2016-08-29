/**
 * 
 */
package com.qdong.communal.library.widget.PopSelectView;

/**
 * 
 * popwindow 区域选择控件的 item选中事件接口
 * @author zd
 *
 */
public interface IPopSelectionListener<T> {

	public void onSelectItem(T item, int selecionType);
}
