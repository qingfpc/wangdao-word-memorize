import { PREFERENCE_NAME } from "../init/DatabaseConfig";
import { preferences } from "@kit.ArkData";
import { GlobalContext } from "../../util/GlobalContext";
import { Context } from "@kit.AbilityKit";

export const getPreferences =()=>{
  const context = GlobalContext.getContext().getObject('context') as Context
  return preferences.getPreferencesSync(context, {name:PREFERENCE_NAME});
}