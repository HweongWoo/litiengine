package de.gurkenlabs.utiliti.swing.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import de.gurkenlabs.litiengine.Resources;
import de.gurkenlabs.litiengine.entities.Direction;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObject;
import de.gurkenlabs.litiengine.environment.tilemap.MapObjectProperty;
import de.gurkenlabs.litiengine.graphics.ImageCache;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.CreatureAnimationController;
import de.gurkenlabs.litiengine.util.ImageProcessing;
import de.gurkenlabs.utiliti.swing.LabelListCellRenderer;

@SuppressWarnings("serial")
public class CreaturePanel extends PropertyPanel<IMapObject> {
  private final JComboBox<JLabel> comboBoxSpriteSheets;
  private final JComboBox<Direction> comboBoxDirection;
  private final JTextField textFieldType;

  /**
   * Create the panel.
   */
  public CreaturePanel() {
    TitledBorder border = new TitledBorder(new LineBorder(new Color(128, 128, 128)), Resources.get("panel_creature"), TitledBorder.LEADING, TitledBorder.TOP, null, null);
    border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
    setBorder(border);

    JLabel lblSprite = new JLabel(Resources.get("panel_sprite"));

    this.comboBoxSpriteSheets = new JComboBox<>();
    this.comboBoxSpriteSheets.setRenderer(new LabelListCellRenderer());

    this.comboBoxDirection = new JComboBox<>();
    this.comboBoxDirection.setModel(new DefaultComboBoxModel<>(Direction.values()));

    JLabel label = new JLabel("direction");

    textFieldType = new JTextField();
    textFieldType.setColumns(10);

    JLabel lblType = new JLabel("type");
    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addContainerGap()
          .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(groupLayout.createSequentialGroup()
              .addComponent(lblSprite, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(comboBoxSpriteSheets, 0, 227, Short.MAX_VALUE))
            .addGroup(groupLayout.createSequentialGroup()
              .addComponent(lblType, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(textFieldType, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(label, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(ComponentPlacement.RELATED)
              .addComponent(comboBoxDirection, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
          .addContainerGap())
    );
    groupLayout.setVerticalGroup(
      groupLayout.createParallelGroup(Alignment.LEADING)
        .addGroup(groupLayout.createSequentialGroup()
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(comboBoxSpriteSheets, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(lblSprite, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE))
          .addPreferredGap(ComponentPlacement.RELATED)
          .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            .addComponent(textFieldType, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
            .addComponent(lblType, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
            .addComponent(label, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
            .addComponent(comboBoxDirection, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
          .addContainerGap(233, Short.MAX_VALUE))
    );
    setLayout(groupLayout);
    this.setupChangedListeners();
  }

  public static String getCreatureSpriteName(String name) {
    if (name.endsWith(CreatureAnimationController.IDLE)) {
      return name.substring(0, name.length() - CreatureAnimationController.IDLE.length());
    }

    if (name.endsWith(CreatureAnimationController.WALK)) {
      return name.substring(0, name.length() - CreatureAnimationController.WALK.length());
    }

    if (name.endsWith(CreatureAnimationController.DEAD)) {
      return name.substring(0, name.length() - CreatureAnimationController.DEAD.length());
    }

    for (Direction dir : Direction.values()) {
      String idle = CreatureAnimationController.IDLE + "-" + dir.toString().toLowerCase();
      if (name.endsWith(idle)) {
        return name.substring(0, name.length() - idle.length());
      }

      String walk = CreatureAnimationController.WALK + "-" + dir.toString().toLowerCase();
      if (name.endsWith(walk)) {
        return name.substring(0, name.length() - walk.length());
      }
    }

    return null;
  }

  @Override
  public void bind(IMapObject mapObject) {
    this.isFocussing = true;
    this.loadAvailableCreatureSprites();
    if (mapObject != null) {
      this.setControlValues(mapObject);
    }

    this.isFocussing = false;
    super.bind(mapObject);
  }

  @Override
  protected void clearControls() {
    this.comboBoxSpriteSheets.setSelectedItem(null);
    this.textFieldType.setText(null);
    this.comboBoxDirection.setSelectedItem(Direction.UNDEFINED);
  }

  @Override
  protected void setControlValues(IMapObject mapObject) {
    if (mapObject.getCustomProperty(MapObjectProperty.SPRITESHEETNAME) != null) {
      for (int i = 0; i < this.comboBoxSpriteSheets.getModel().getSize(); i++) {
        JLabel label = this.comboBoxSpriteSheets.getModel().getElementAt(i);
        if (label != null && label.getText().equals(mapObject.getCustomProperty(MapObjectProperty.SPRITESHEETNAME))) {
          this.comboBoxSpriteSheets.setSelectedItem(label);
          break;
        }
      }
    }

    this.textFieldType.setText(mapObject.getCustomProperty(MapObjectProperty.SPAWN_TYPE));
    this.comboBoxDirection.setSelectedItem(mapObject.getCustomPropertyEnum(MapObjectProperty.SPAWN_DIRECTION, Direction.class, Direction.UNDEFINED));
  }

  private void setupChangedListeners() {
    this.comboBoxSpriteSheets.addActionListener(new MapObjectPropertyActionListener(m -> {
      JLabel selected = (JLabel) this.comboBoxSpriteSheets.getSelectedItem();
      m.setCustomProperty(MapObjectProperty.SPRITESHEETNAME, selected.getText());
    }));

    this.comboBoxDirection.addActionListener(new MapObjectPropertyActionListener(m -> {
      m.setCustomProperty(MapObjectProperty.SPAWN_DIRECTION, this.comboBoxDirection.getSelectedItem().toString());
    }));

    this.textFieldType.addFocusListener(new MapObjectPropteryFocusListener(m -> m.setCustomProperty(MapObjectProperty.SPAWN_TYPE, textFieldType.getText())));
    this.textFieldType.addActionListener(new MapObjectPropertyActionListener(m -> m.setCustomProperty(MapObjectProperty.SPAWN_TYPE, textFieldType.getText())));
  }

  private void loadAvailableCreatureSprites() {
    Map<String, String> m = new TreeMap<>();
    for (Spritesheet s : Spritesheet.getSpritesheets()) {
      String creatureSpriteName = getCreatureSpriteName(s.getName());
      if (creatureSpriteName != null) {
        if (!m.containsKey(creatureSpriteName)) {
          m.put(creatureSpriteName, s.getName());
        }
      }
    }

    this.comboBoxSpriteSheets.removeAllItems();
    for (Map.Entry<String, String> entry : m.entrySet()) {
      JLabel label = new JLabel();
      label.setText(entry.getKey());
      String value = entry.getValue();
      Spritesheet sprite = Spritesheet.find(value);
      if (sprite != null && sprite.getTotalNumberOfSprites() > 0) {
        BufferedImage img = sprite.getSprite(0);

        BufferedImage scaled;
        String cacheKey = "iconx24" + sprite.getName();
        if (ImageCache.SPRITES.containsKey(cacheKey)) {
          scaled = ImageCache.SPRITES.get(cacheKey);
        } else {
          if (img != null) {
            scaled = ImageProcessing.scaleImage(img, 24, 24, true);
          } else {
            scaled = ImageProcessing.getCompatibleImage(24, 24);
          }

          ImageCache.SPRITES.put(cacheKey, scaled);
        }

        if (scaled != null) {
          label.setIcon(new ImageIcon(scaled));
        }
      }

      this.comboBoxSpriteSheets.addItem(label);
    }
  }
}