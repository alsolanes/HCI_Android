package project.hci.keepmytown;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by alsol on 8/6/2016.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] urls, coords;
    private Bitmap[] bitmaps;
    private Activity context;

    public CustomList(Activity context, String[] urls, Bitmap[] bitmaps, String[] coords){
        super(context, R.layout.image_list_view,urls);
        this.context = context;
        this.urls = urls;
        this.bitmaps = bitmaps;
        this.coords = coords;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.image_list_view, null, true);
        TextView textViewURL = (TextView) listViewItem.findViewById(R.id.textViewURL);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageDownloaded);
        TextView coord = (TextView) listViewItem.findViewById(R.id.textLatLng);

        textViewURL.setText(urls[position]);
        coord.setText(coords[position]);
        image.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position],100,50,false));
        return listViewItem;
    }
}
