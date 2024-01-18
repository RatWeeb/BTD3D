package me.ChristopherW.core.custom.UI.UIScreens;

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import java.awt.Color;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import me.ChristopherW.core.ObjectLoader;
import me.ChristopherW.core.custom.Tower;
import me.ChristopherW.core.custom.TowerType;
import me.ChristopherW.core.custom.Upgrade;
import me.ChristopherW.core.custom.UI.GUIManager;
import me.ChristopherW.core.custom.UI.IGUIScreen;
import me.ChristopherW.core.entity.Texture;
import me.ChristopherW.process.Game;
import me.ChristopherW.process.Launcher;

public class GameplayScreen implements IGUIScreen {

    Texture gameSpeedButton;
    Texture gameSpeedButtonActive;
    Texture panel;
    Texture coin;
    Texture heart;
    Texture primarySlot;
    Texture militarySlot;
    Texture upgrade;
    Texture upgradeDisabled;
    Texture upgradePanel;
    Texture shop;
    boolean gameSpeedToggled = false;
    Texture[] tower_icons = new Texture[8];

    private void textOutline(String text, Color fill, Color outline) {
        ImVec2 currentCursor = new ImVec2(ImGui.getCursorPos());
        int size = 2;
        ImGui.setCursorPos(currentCursor.x - size,  currentCursor.y - size);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x + size,  currentCursor.y - size);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x - size,  currentCursor.y + size);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x + size,  currentCursor.y + size);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x,  currentCursor.y - size);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x,  currentCursor.y + size);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x - size,  currentCursor.y);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);
        ImGui.setCursorPos(currentCursor.x + size,  currentCursor.y);
        ImGui.textColored(outline.getRed(), outline.getBlue(), outline.getGreen(), outline.getAlpha(), text);

        ImGui.setCursorPos(currentCursor.x, currentCursor.y);
        ImGui.textColored(fill.getRed(), fill.getBlue(), fill.getGreen(), fill.getAlpha(), text);
    }

    @Override
    public void start() {
        ObjectLoader loader = new ObjectLoader();
        panel = loader.createTexture("assets/textures/panel.png");
        coin = loader.createTexture("assets/textures/cash.png");
        heart = loader.createTexture("assets/textures/lives.png");
        shop = loader.createTexture("assets/textures/shop_large.png");
        primarySlot = loader.createTexture("assets/textures/primary_slot.png");
        militarySlot = loader.createTexture("assets/textures/military_slot.png");
        gameSpeedButton = loader.createTexture("assets/textures/ff_icon.png");
        gameSpeedButtonActive = loader.createTexture("assets/textures/ff_icon_active.png");
        upgrade = loader.createTexture("assets/textures/upgrade.png");
        upgradeDisabled = loader.createTexture("assets/textures/upgrade_disabled.png");
        upgradePanel = loader.createTexture("assets/textures/upgrade_panel.png");

        tower_icons[0] = loader.createTexture("assets/textures/dart_monkey.png");
        tower_icons[1] = loader.createTexture("assets/textures/sniper_monkey.png");
        tower_icons[2] = loader.createTexture("assets/textures/bomb_shooter.png");
    }

    @Override
    public void render(ImBoolean p_open, GUIManager gm) {
        Game game = Launcher.getGame();
        float panelWidth = gm.window.getHeight() * 0.3f;
        float panelHeight = gm.window.getHeight();

        if(game.monkeyMode > 0) {
            ImGui.setNextWindowPos((int)gm.window.getInput().getCurrentPos().x,(int)gm.window.getInput().getCurrentPos().y, 0, 1,0);
            if (ImGui.begin("gameplay_mouse", p_open, gm.window_flags)) {
                ImGui.pushFont(gm.monkeyFontMedium);
                //ImGui.image(panel.getId(), 71, 32);
                TowerType selectedMonkey = TowerType.values()[game.monkeyMode - 1];
                String cost = "$" + String.valueOf(selectedMonkey.cost);
                ImVec2 dimensions = ImGui.calcTextSize(cost);
                ImGui.setCursorPos(35-(dimensions.x/3), 14);
                
                Color costColor;
                if(game.player.getMoney() >= selectedMonkey.cost)
                    costColor = Color.white;
                else
                    costColor = Color.red;
                textOutline(cost, costColor, Color.black);
                ImGui.popFont();
            }
            ImGui.end();
        }

        ImGui.pushFont(gm.monkeyFontMedium);
        ImGui.setNextWindowBgAlpha(0f);
        ImGui.setNextWindowSize(0, 0);
        ImGui.setNextWindowPos(0,0, 0, 0,0);
        if (ImGui.begin("gameplay_L", p_open, gm.window_flags)) {
            ImGui.image(heart.getId(),32, 28);
            ImGui.sameLine();
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 5);
            textOutline(String.valueOf(game.player.getLives()), Color.white, Color.black);
            ImGui.sameLine();
            ImGui.setCursorPosY(ImGui.getCursorPosY() - 5);
            ImGui.setCursorPosX(ImGui.getCursorPosX() + 10);
            ImGui.image(coin.getId(),32, 30);
            ImGui.sameLine();
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 5);
            textOutline("$" + String.valueOf(game.player.getMoney()), Color.white, Color.black);
            ImGui.sameLine();
            ImGui.popFont();
            ImGui.pushFont(gm.monkeyFontSmall);
            ImVec2 roundDim = ImGui.calcTextSize("Round");
            ImGui.setCursorPosX(gm.window.getWidth() - panelWidth - roundDim.x - gm.window.getWidth()/128);
            ImVec2 pos = new ImVec2(ImGui.getCursorPos());
            String roundNumber = "1";
            ImVec2 roundNumDim = ImGui.calcTextSize(roundNumber);
            textOutline("Round", Color.white, Color.black);
            ImGui.setCursorPos(pos.x + roundDim.x - roundNumDim.x - gm.window.getWidth()/128, pos.y + 25);
            ImGui.popFont();
            ImGui.pushFont(gm.monkeyFont);
            textOutline(roundNumber, Color.white, Color.black);
        }
        ImGui.end();

        ImGui.setNextWindowPos(gm.window.getWidth() + 5,gm.window.getHeight() + 5, 0, 1,1);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowMinSize, 0,0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0,0);
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 0,0);
        if (ImGui.begin("gameplay_R", p_open, gm.window_flags)) {
            ImGui.image(shop.getId(), panelWidth, panelHeight);



            ImGui.popFont();
            ImGui.pushFont(gm.monkeyFontSmall);
            String title = game.monkeyMode > 0 ? TowerType.values()[game.monkeyMode - 1].name : "Shop";
            ImVec2 textDim = ImGui.calcTextSize(title);
            ImGui.setCursorPos(panelWidth/2 - textDim.x/2, panelHeight/24);
            textOutline(title, Color.white, Color.black);

            int currentX = 0;
            int currentY = 0;
            float towerImageSize = panelHeight/10;
            for(int i = 0; i < TowerType.values().length; i++) {
                TowerType t = TowerType.values()[i];

                ImGui.setCursorPos(panelWidth/7 + (currentX * (towerImageSize + gm.window.getWidth()/256)), panelHeight/10 + (currentY * (towerImageSize + gm.window.getHeight()/32)));
                ImVec2 oldCursor = ImGui.getCursorPos();
                switch (t.towerClass) {
                    case PRIMARY:
                        ImGui.image(primarySlot.getId(), towerImageSize, towerImageSize);
                        break;
                
                    case MILITARY:
                        ImGui.image(militarySlot.getId(), towerImageSize, towerImageSize);
                        break;
                }
                ImGui.setCursorPos(oldCursor.x, oldCursor.y);

                ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1, 1, 1, 0.25f);
                if(ImGui.imageButton(tower_icons[i].getId(), towerImageSize, towerImageSize)) {
                    game.monkeyMode = (Integer.valueOf(i) + 1);
                }
                ImGui.popStyleColor(3);
                Color costColor;
                if(game.player.getMoney() >= TowerType.values()[i].cost)
                    costColor = Color.white;
                else
                    costColor = Color.RED;
                String cost = "$" + String.valueOf(TowerType.values()[i].cost);
                ImVec2 costSize = ImGui.calcTextSize(cost);
                ImGui.setCursorPos(oldCursor.x + towerImageSize/2 - costSize.x/2, oldCursor.y + towerImageSize);
                textOutline(cost, costColor, Color.black);

                currentX++;
                if(currentX > 1) {
                    currentX = 0;
                    currentY++;
                }
            }

            ImGui.setCursorPosY(ImGui.getCursorPosY() + 100);
            float upgradeSize = panelHeight * 0.1f;
            float upgradeIconSize = panelHeight * 0.075f;
            if(game.currentTowerInspecting != null) {
                Tower selected = game.currentTowerInspecting;
                ImGui.setCursorPosX(ImGui.getCursorPosX() + panelWidth/2 - (upgradeSize * 1.25f));
                ImGui.image(upgradePanel.getId(), upgradeSize * 1.25f, upgradeSize);

                Upgrade nextUpgrade1 = selected.getPath1().nextUpgrade;
                int costValue1 = Integer.MAX_VALUE;
                int ordinal1 = 4;
                String name1 = "";
                if(nextUpgrade1 != null) {
                    costValue1 = nextUpgrade1.cost;
                    ordinal1 = nextUpgrade1.ordinal();
                    name1 = nextUpgrade1.name;
                }
                boolean affordable = costValue1 <= game.player.getMoney();
                ImGui.sameLine();
                ImGui.setCursorPosX(ImGui.getCursorPosX() - 15);
                ImVec2 old = ImGui.getCursorPos();
                ImGui.image(affordable ? upgrade.getId() : upgradeDisabled.getId(), upgradeSize * 1.25f, upgradeSize);
                ImGui.setCursorPos(old.x + (upgradeSize * 0.25f) + (upgradeSize/2 - upgradeIconSize/2), old.y + (upgradeSize/2 - upgradeIconSize/2));

                ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1, 1, 1, affordable ? 0.25f : 0f);
                if(ImGui.imageButton(Game.upgradeTextures[ordinal1].getId(), upgradeIconSize, upgradeIconSize)) {
                    if(nextUpgrade1 != null && affordable) {
                        game.player.removeMoney(selected.getPath1().nextUpgrade.cost);
                        selected.upgradePath(1);
                        Game.audioSources.get("upgrade").play();
                    }
                }
                ImGui.popStyleColor(3);

                ImGui.popFont();
                ImGui.pushFont(gm.monkeyFontTiny);
                String upgradeName = name1;
                ImVec2 upgradeNameDim = ImGui.calcTextSize(upgradeName);
                ImGui.setCursorPos(old.x + upgradeSize/2 - upgradeNameDim.x/2 + (upgradeSize * 0.25f), old.y);
                textOutline(upgradeName, Color.white, Color.black);
                ImGui.popFont();
                ImGui.pushFont(gm.monkeyFontSmall);
                String cost = costValue1 == Integer.MAX_VALUE ? "" : "$" + String.valueOf(costValue1);
                ImVec2 costDim = ImGui.calcTextSize(cost);
                ImGui.setCursorPos(old.x + upgradeSize/2 - costDim.x/2 + (upgradeSize * 0.25f), old.y + upgradeSize);
                textOutline(cost, affordable ? Color.white : Color.red, Color.black);

                ImGui.setCursorPosY(ImGui.getCursorPosY() + gm.window.getHeight()/64);

                Upgrade nextUpgrade2 = selected.getPath2().nextUpgrade;
                int costValue2 = Integer.MAX_VALUE;
                int ordinal2 = 4;
                String name2 = "";
                if(nextUpgrade2 != null) {
                    costValue2 = nextUpgrade2.cost;
                    ordinal2 = nextUpgrade2.ordinal();
                    name2 = nextUpgrade2.name;
                }
                boolean affordable2 = costValue2 <= game.player.getMoney();
                ImGui.popFont();
                ImGui.pushFont(gm.monkeyFontTiny);
                ImGui.setCursorPosX(ImGui.getCursorPosX() + panelWidth/2 - (upgradeSize * 1.25f));
                ImGui.image(upgradePanel.getId(), upgradeSize * 1.25f, upgradeSize);
                ImGui.sameLine();
                ImGui.setCursorPosX(ImGui.getCursorPosX() - 15);
                old = ImGui.getCursorPos();
                ImGui.image(affordable2 ? upgrade.getId() : upgradeDisabled.getId(), upgradeSize * 1.25f, upgradeSize);
                ImGui.setCursorPos(old.x + (upgradeSize * 0.25f) + (upgradeSize/2 - upgradeIconSize/2), old.y + (upgradeSize/2 - upgradeIconSize/2));

                ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1, 1, 1, affordable2 ? 0.25f : 0f);
                if(ImGui.imageButton(Game.upgradeTextures[ordinal2].getId(), upgradeIconSize, upgradeIconSize)) {
                    if(nextUpgrade2 != null && affordable2) {
                        game.player.removeMoney(selected.getPath2().nextUpgrade.cost);
                        selected.upgradePath(2);
                        Game.audioSources.get("upgrade").play();
                    }
                }
                ImGui.popStyleColor(3);

                String upgradeName2 = name2;
                ImVec2 upgradeNameDim2 = ImGui.calcTextSize(upgradeName2);
                ImGui.setCursorPos(old.x + upgradeSize/2 - upgradeNameDim2.x/2 + (upgradeSize * 0.25f), old.y);
                textOutline(upgradeName2, Color.white, Color.black);
                ImGui.popFont();
                ImGui.pushFont(gm.monkeyFontSmall);
                String cost2 = costValue2 == Integer.MAX_VALUE ? "" : "$" + String.valueOf(costValue2);
                ImVec2 costDim2 = ImGui.calcTextSize(cost2);
                ImGui.setCursorPos(old.x + upgradeSize/2 - costDim2.x/2 + (upgradeSize * 0.25f), old.y + upgradeSize);
                textOutline(cost2, affordable2 ? Color.white : Color.red, Color.black);
            }

            float imageSize = panelHeight * 0.1388f;
            ImGui.setCursorPos(panelWidth/2 - imageSize/2, panelHeight - panelHeight/5);
            ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0, 0, 0, 0);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0, 0, 0, 0);
            if (ImGui.imageButton((gameSpeedToggled) ? gameSpeedButtonActive.getId() : gameSpeedButton.getId(), imageSize, imageSize, 0, 0, 1, 1, 0, 0, 0, 0, 0)) {
                gameSpeedToggled = !gameSpeedToggled;
                game.gameSpeed = (gameSpeedToggled) ? 2f : 1f;
            }
            ImGui.popStyleColor(3);
        }
        ImGui.popStyleVar(3);
        
        ImGui.popFont();
        ImGui.end();
    }
}
