package com.example.stockapp;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 *This is the Expandable List adapter which is the adapter of how a list element of the expandable list
 * looks alike. For each list element, this is the custom Adapter on how it will look and how it will open.
 * After adding the data to the list, we set the adapter to the list to be displayed.
 * When we open, Market Sentiment, we are able to see 3 lists which can be opened by clicking
 * on the list elements.
 * Referred from: https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    /*
    List Data header will contain all the heading elements.
    List Data child would contain a List data header with a list of child's mapped to each data header.

    Example:
    Header = 1 and when we click on 1, 2,3,4 (list elements) opens which is a list mapped to 1 in listDataChild.
     */
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    //Constructor
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    //Getting the Object of the list element
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    //getting the position of the child
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //Inflating the view of the child element upon clicking the header and closing it when clicking it again.
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        //If current view is null, just inflating the view.
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        //Setting the textView of the child. If you ctrl+click on the lbListItem, you can see
        //the textview to which data is being set here.
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return convertView;
    }

    //Returning the size of the children
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    //Returning the group according to the position as an object
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    //returning the group count [Headers count]
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    //Getting position of the group i.e the group ID.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /*
    This is for the setting up the view of the headers.
    The headers are being inflated here and the text is being set to the lbLlistHeader.

     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //Getting header position
        String headerTitle = (String) getGroup(groupPosition);

        //Inflating the view if it is null
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        //Setting the TextView of the headers
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
