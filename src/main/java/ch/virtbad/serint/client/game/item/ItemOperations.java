package ch.virtbad.serint.client.game.item;

import ch.virtbad.serint.client.engine.content.MeshHelper;
import ch.virtbad.serint.client.game.map.data.TextureLocation;
import ch.virtbad.serint.client.graphics.ResourceHandler;

/**
 * @author Virt
 */
public class ItemOperations {

    /**
     * Get the texture location by an item type
     * @param id item type id
     * @return location of the texture
     */
    public static TextureLocation getTexture(int id){
       ItemType[] items = ResourceHandler.getData().getItems();
       for(ItemType item: items) if(item.getId() == id) {
           return item.getTexture();
       }
       return new TextureLocation(0, 0);
    }

    public static float[] getVertices(int id){
        TextureLocation tex = getTexture(id);

        return new float[] {
                0, 0, tex.getX()    , tex.getY() + 1,
                1, 0, tex.getX() + 1, tex.getY() + 1,
                0, 1, tex.getX()    , tex.getY(),
                1, 1, tex.getX() + 1, tex.getY()
        };
    }

    public static int[] getIndices(){
        return MeshHelper.createQuadIndices();
    }


}
